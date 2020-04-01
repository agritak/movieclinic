package spring.movieclinic;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoryRepository extends CrudRepository <Category, Integer> {
    List<Category> findByNameContains(String name);

    List<Category> findByOrderByNameAsc();

}
