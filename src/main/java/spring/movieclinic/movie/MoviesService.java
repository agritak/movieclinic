package spring.movieclinic.movie;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import spring.movieclinic.category.Category;
import spring.movieclinic.category.CategoryRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class MoviesService {

    private final MovieRepository movieRepository;
    private final CategoryRepository categoryRepository;

    public List<Movie> getMoviesByNameAsc() {
        return movieRepository.findByOrderByNameAsc();
    }

    public Page<Movie> paginateMovies(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }

    public Page<Movie> paginateAnyMoviesList(Pageable pageable, List<Movie> movies) {
        int size = pageable.getPageSize();
        int page = pageable.getPageNumber();
        int firstItem = page * size;
        List<Movie> content;

        if (movies.size() < firstItem) {
            content = Collections.emptyList();
        } else {
            int toIndex = Math.min(firstItem + size, movies.size());
            content = movies.subList(firstItem, toIndex);
        }
        return new PageImpl<>(content, pageable, movies.size());
    }

    public Movie findMovieById(Integer id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid movie Id:" + id));
    }

    void create(FrontMovie frontMovie) {
        Movie movie = new Movie(frontMovie, findCategoriesById(frontMovie.getCategories()));
        movieRepository.save(movie);
    }

    void update(Integer id, FrontMovie frontMovie) {
        Movie movie = findMovieById(id);
        movie.update(frontMovie, findCategoriesById(frontMovie.getCategories()));
        movieRepository.save(movie);
    }

    void delete(Integer id) {
        movieRepository.deleteById(id);
    }

    Optional<Movie> findMovieByNameAndYear(String name, Integer year) {
        return movieRepository.findByNameAndYear(name, year);
    }

    private Set<Category> findCategoriesById(Set<Integer> ids) {
        return categoryRepository.findByIdIn(ids);
    }

}
