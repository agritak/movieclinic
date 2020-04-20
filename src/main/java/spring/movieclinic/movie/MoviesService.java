package spring.movieclinic.movie;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
        //TODO duplicate code start (vajadzētu iznest uz atsevišķu metodi)
        m.setName(movie.getName());
        m.setDescription(movie.getDescription());
        m.setYear(movie.getYear());
        m.setPictureURL(movie.getPictureURL());
        m.setTrailerURL(movie.getTrailerURL());
        m.setCategories(movie.getCategories());
        //TODO duplicate code end
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
        //findById(id);
        movieRepository.deleteById(id);
    }

    public Movie findById(Integer id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid movie Id:" + id));
    }

    List<Movie> search(String keyword) {
        return movieRepository.findByNameContains(keyword);
    }
}
