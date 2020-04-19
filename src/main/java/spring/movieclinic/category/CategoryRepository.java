package spring.movieclinic.category;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends CrudRepository <Category, Integer> {
    List<Category> findByNameContains(String name);

    List<Category> findByOrderByNameAsc();

    Optional<Category> findByName(String name);
}
