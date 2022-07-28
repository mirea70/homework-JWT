package com.example.homeworkjwt.config;

import com.example.homeworkjwt.filter.FirstFilter;
import com.example.homeworkjwt.filter.JwtAuthenticationFilter;
import com.example.homeworkjwt.filter.jwtAuthorizationFilter;
import com.example.homeworkjwt.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CorsFilter corsFilter;
    private final MemberRepository memberRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.addFilterBefore(new FirstFilter(), BasicAuthenticationFilter.class);
        http.csrf().disable();
        http.headers().frameOptions().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .apply(new CustomDsl())
                .and()
                .authorizeRequests()
                .antMatchers("/api/v1/user/**")
                .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/manager/**")
                .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/admin/**")
                .access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()
        ;
        return http.build();
    }
    public class CustomDsl extends AbstractHttpConfigurer<CustomDsl, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception {
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
            builder
                    .addFilter(corsFilter)
                    .addFilter(new JwtAuthenticationFilter(authenticationManager))
                    .addFilter(new jwtAuthorizationFilter(authenticationManager, memberRepository));
        }
    }
}
