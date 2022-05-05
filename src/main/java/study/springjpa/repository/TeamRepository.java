package study.springjpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.springjpa.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
}