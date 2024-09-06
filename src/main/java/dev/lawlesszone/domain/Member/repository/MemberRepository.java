package dev.lawlesszone.domain.Member.repository;

import dev.lawlesszone.domain.Member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    
}
