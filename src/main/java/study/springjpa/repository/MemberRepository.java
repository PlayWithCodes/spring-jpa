package study.springjpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.springjpa.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsernameAndAgeGreaterThan(String name, int age);
}
