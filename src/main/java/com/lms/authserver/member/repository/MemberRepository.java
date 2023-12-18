package com.lms.authserver.member.repository;

import com.lms.authserver.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID> {
    Optional<Member> findByEmail(String email);

    @Query("select m.email from Member as m where m.id = :memberId")
    String findUserEmailById(@Param("memberId") UUID memberId);
}
