package study.springjpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.springjpa.entity.Item;

public interface ItemRepository extends JpaRepository<Item, String> {
}
