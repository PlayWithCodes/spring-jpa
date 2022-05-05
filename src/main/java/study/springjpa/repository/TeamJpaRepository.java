package study.springjpa.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import study.springjpa.entity.Member;
import study.springjpa.entity.Team;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public class TeamJpaRepository {

    @Autowired
    private EntityManager entityManager;

    public Team save(Team team) {
        entityManager.persist(team);
        return team;
    }

    public void delete(Member member) {
        entityManager.remove(member);
    }

    public List<Team> findAll() {
        return entityManager.createQuery(
                "select t from Team t", Team.class
        ).getResultList();
    }

    public Optional<Team> findById(Long id) {
        Team team = entityManager.find(Team.class, id);
        return Optional.ofNullable(team);
    }

    public long count() {
        return entityManager.createQuery(
                "select count(t) from Team t", Long.class
        ).getSingleResult();
    }
}
