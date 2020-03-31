package spring.movieclinic.movie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MoviesService {

    @Autowired
    private MovieRepository movieRepository;

    public Iterable<Movie> movies() {
        if (movieRepository.findByOrderByTitleAsc().isEmpty()) {
            return null;
        }
        return movieRepository.findByOrderByTitleAsc();
    }

    public Movie create(Movie movie) {
        if (movieRepository.count() > 0) {
            for (Movie m : movies()) {
                if (m.equals(movie)) {
                    return null;
                }
            }
        }

        movieRepository.save(movie);
        return movie;
    }

    public void update(Integer id, Movie movie) {
        Optional<Movie> optional = movieRepository.findById(id);
        if (optional.isPresent()) {
            Movie m = optional.get();
            m.setTitle(movie.getTitle());
            m.setPlot(movie.getPlot());
            m.setYear(movie.getYear());
            m.setPictureURL(movie.getPictureURL());
            m.setTrailerURL(movie.getTrailerURL());
            movieRepository.save(m);
        }
    }

    public void delete(Integer id) {
        movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid movie Id:" + id));
        movieRepository.deleteById(id);
    }

    public Movie findById(Integer id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid movie Id:" + id));
    }

    public Iterable<Movie> search(String keyword) {
        return movieRepository.findByTitleContains(keyword);
    }
}
