package spring.movieclinic.movie;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MovieRepository extends CrudRepository<Movie, Integer> {
    List<Movie> findByTitleContains(String name);

    List<Movie> findByOrderByTitleAsc();
}
