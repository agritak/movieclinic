package spring.movieclinic.movie;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import spring.movieclinic.category.CategoryRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MoviesService {

    private final MovieRepository movieRepository;
    private final CategoryRepository categoryRepository;

    public List<Movie> movies() {
        return movieRepository.findByOrderByNameAsc();
    }

    public List<Movie> moviesShuffled() {
        List<Movie> movies = (List<Movie>) movieRepository.findAll();
        Collections.shuffle(movies);
        return movies;
    }

    public Movie findMovie(Integer id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid movie Id:" + id));
    }

    void create(FrontMovie frontMovie) {
        movieRepository.save(frontMovieToMovie(frontMovie, new Movie()));
    }

    void update(Integer id, FrontMovie frontMovie) {
        movieRepository.save(frontMovieToMovie(frontMovie, findMovie(id)));
    }

    void delete(Integer id) {
        Optional<Movie> delete = movieRepository.findById(id);
        delete.ifPresent(movieRepository::delete);
        //movieRepository.deleteById(id);
    }

    Optional<Movie> movieExists(String name, Integer year) {
        return movieRepository.findByNameAndYear(name, year);
    }

    Movie frontMovieToMovie(FrontMovie frontMovie, Movie movie) {
        movie.setName(frontMovie.getName());
        movie.setDescription(frontMovie.getDescription());
        movie.setYear(frontMovie.getYear());
        movie.setPictureURL(frontMovie.getPictureURL());
        movie.setTrailerURL(frontMovie.getTrailerURL());
        movie.setCategories(categoryRepository.findByIdIn(frontMovie.getCategories()));
        return movie;
    }
}
