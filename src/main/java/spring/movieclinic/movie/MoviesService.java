package spring.movieclinic.movie;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MoviesService {

    private final MovieRepository movieRepository;

    List<Movie> movies() {
        return movieRepository.findByOrderByNameAsc();
    }

    void create(Movie movie) {
        movieRepository.save(movie);
    }

    void update(Integer id, Movie movie) {
        //Movie m = findById(id);
        movie.setId(id);
        movieRepository.save(movie);
    }

    void delete(Integer id) {
        //findById(id);
        movieRepository.deleteById(id);
    }

    Movie findById(Integer id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid movie Id:" + id));
    }

    List<Movie> search(String keyword) {
        return movieRepository.findByNameContains(keyword);
    }
}
