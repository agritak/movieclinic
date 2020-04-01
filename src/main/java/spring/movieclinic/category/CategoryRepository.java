package spring.movieclinic.category;

import org.springframework.data.repository.CrudRepository;
import spring.movieclinic.category.Category;

import java.util.List;

public interface CategoryRepository extends CrudRepository <Category, Integer> {
    List<Category> findByNameContains(String name);

    List<Category> findByOrderByNameAsc();

}
