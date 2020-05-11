package spring.movieclinic.movie;

import org.springframework.data.repository.CrudRepository;
import spring.movieclinic.category.Category;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MovieRepository extends CrudRepository<Movie, Integer> {

    List<Movie> findByNameContains(String name);

    List<Movie> findByYear(Integer year);

    List<Movie> findByCategoriesIn(Set<Category> categories);

    List<Movie> findByDescriptionContains(String description);

    List<Movie> findByOrderByNameAsc();

    Optional<Movie> findByNameAndYear(String name, Integer year);

    List<Movie> findByNameInAndYearIn(List<String> names, List<Integer> years);

}
