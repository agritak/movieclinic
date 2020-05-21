package spring.movieclinic.movie;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import spring.movieclinic.category.Category;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MovieRepository extends PagingAndSortingRepository<Movie, Integer> {

    List<Movie> findByNameContains(String name);

    @Query(value = "SELECT * FROM movies movie " +
            "INNER JOIN movie_category m_c " +
            "ON (movie.id = m_c.movie_id " +
            "AND ((COALESCE(:categories, null)) IS NULL " +
            "OR m_c.category_id IN :categories))" +
            "WHERE (movie.name LIKE %:name%) " +
            "AND (:year IS NULL OR movie.year = :year) " +
            "AND (movie.description LIKE %:description%)", nativeQuery = true)
    List<Movie> findByNameAndYearAndCategoriesAndDescription(@Param("categories") Set<Integer> categories,
                                                             @Param("name") String name,
                                                             @Param("year") Integer year,
                                                             @Param("description") String description);

    List<Movie> findByOrderByNameAsc();

    Optional<Movie> findByNameAndYear(String name, Integer year);

    List<Movie> findByNameInAndYearIn(List<String> names, List<Integer> years);

    Page<Movie> findAll(Pageable pageable);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM movies WHERE id = ?")
    void deleteById(Integer id);
}
