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

    void create(FrontMovie frontMovie) {
        movieRepository.save(frontMovieToMovie(frontMovie, new Movie()));
    }

    void update(Integer id, FrontMovie frontMovie) {
        movieRepository.save(frontMovieToMovie(frontMovie, findById(id)));
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

    private Movie frontMovieToMovie(FrontMovie frontMovie, Movie movie) {
        movie.setName(frontMovie.getName());
        movie.setDescription(frontMovie.getDescription());
        movie.setYear(frontMovie.getYear());
        movie.setPictureURL(frontMovie.getPictureURL());
        movie.setTrailerURL(frontMovie.getTrailerURL());
        movie.setCategories(frontMovie.getCategories());
        return movie;
    }
}
