package study.springjpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.springjpa.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
