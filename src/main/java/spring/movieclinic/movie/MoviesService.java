package spring.movieclinic.movie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class MoviesService {

    @Autowired
    private MovieRepository movieRepository;

    public List<Movie> movies() {
        if (movieRepository.findByOrderByNameAsc().isEmpty()) {
            return null;
        }
        return movieRepository.findByOrderByNameAsc();
    }

    public void create(Movie movie) {
        if (movieRepository.count() > 0) {
            for (Movie m : movies()) {
                if (m.equals(movie)) {
                    return;
                }
            }
        }
        movieRepository.save(movie);
    }

    public void update(Integer id, Movie movie) {
        Movie m = findById(id);
        movie.setId(id);
        movie.setCategories(m.getCategories());
        movieRepository.save(movie);
    }

    public void delete(Integer id) {
        findById(id);
        movieRepository.deleteById(id);
    }

    public Movie findById(Integer id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid movie Id:" + id));
    }

    public List<Movie> search(String keyword) {
        return movieRepository.findByNameContains(keyword);
    }
}
