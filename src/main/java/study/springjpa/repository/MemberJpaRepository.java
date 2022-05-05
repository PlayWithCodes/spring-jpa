package study.springjpa.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.springjpa.entity.Member;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class MemberJpaRepository {

    private final EntityManager entityManager;

    public Member save(Member member) {
        entityManager.persist(member);
        return member;
    }

    public Member find(Long id) {
        return entityManager.find(Member.class, id);
    }
}
