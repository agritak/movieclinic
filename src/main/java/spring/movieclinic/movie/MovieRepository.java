package spring.movieclinic.movie;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import spring.movieclinic.category.Category;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MovieRepository extends PagingAndSortingRepository<Movie, Integer> {

    List<Movie> findByNameContains(String name);

    List<Movie> findByYear(Integer year);

    List<Movie> findByCategoriesIn(Set<Category> categories);

    List<Movie> findByDescriptionContains(String description);

    List<Movie> findByOrderByNameAsc();

    Optional<Movie> findByNameAndYear(String name, Integer year);

    List<Movie> findByNameInAndYearIn(List<String> names, List<Integer> years);

    Page<Movie> findAll(Pageable pageable);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM movies WHERE id = ?")
    void deleteById(Integer id);
}
