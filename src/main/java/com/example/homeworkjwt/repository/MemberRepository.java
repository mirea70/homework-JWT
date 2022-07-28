package com.example.homeworkjwt.repository;

import com.example.homeworkjwt.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    public Member findByUsername(String member);
}
