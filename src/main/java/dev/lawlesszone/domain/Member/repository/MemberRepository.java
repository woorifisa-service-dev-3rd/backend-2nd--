package dev.lawlesszone.domain.Member.repository;

import java.util.Optional;

import dev.lawlesszone.domain.Member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long> {
    <S extends Member> S save(S entity);

    Optional<Member> findByEmail(String email);
}
