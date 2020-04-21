package spring.movieclinic.movie;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class MoviesService {

    private final MovieRepository movieRepository;

    public List<Movie> movies() {
        return movieRepository.findByOrderByNameAsc();
    }

    void create(FrontMovie movie) {
        Movie m = new Movie();
        m.setName(movie.getName());
        m.setDescription(movie.getDescription());
        m.setYear(movie.getYear());
        m.setPictureURL(movie.getPictureURL());
        m.setTrailerURL(movie.getTrailerURL());
        m.setCategories(movie.getCategories());
        movieRepository.save(m);
    }

    void update(Integer id, FrontMovie movie) {
        Movie m = findById(id);
        m.setName(movie.getName());
        m.setDescription(movie.getDescription());
        m.setYear(movie.getYear());
        m.setPictureURL(movie.getPictureURL());
        m.setTrailerURL(movie.getTrailerURL());
        m.setCategories(movie.getCategories());
        movieRepository.save(m);
    }

    void delete(Integer id) {
        movieRepository.deleteById(id);
    }

    public Movie findById(Integer id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid movie Id:" + id));
    }

    public List<Movie> moviesShuffled() {
        List<Movie> movies = movies();
        Collections.shuffle(movies);
        return movies;
    }
}
