package spring.movieclinic.category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import spring.movieclinic.movie.Movie;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer> {

    //extends CrudRepository<Category, Integer>
    List<Category> findByNameContains(String name);

    List<Category> findByOrderByNameAsc();

    Optional<Category> findByName(String name);

    Set<Category> findByNameIn(List<String> names);

    Set<Category> findByIdIn(Set<Integer> ids);

    Page<Category> findAll(Pageable pageable);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM categories WHERE id = ?")
    void deleteById(Integer id);

}
