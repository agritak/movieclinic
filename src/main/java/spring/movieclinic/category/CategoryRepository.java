package spring.movieclinic.category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CategoryRepository extends CrudRepository<Category, Integer> {

    List<Category> findByNameContains(String name);

    List<Category> findByOrderByNameAsc();

    Optional<Category> findByName(String name);

    Set<Category> findByNameIn(List<String> names);

    Set<Category> findByIdIn(Set<Integer> ids);

    Page<Category> findAll(Pageable pageable);

    @Modifying
    @Query("DELETE FROM Category c WHERE c.id = :id")
    void deleteById(Integer id);
}
