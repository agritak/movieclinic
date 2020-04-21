package spring.movieclinic.omdb;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import spring.movieclinic.category.Category;
import spring.movieclinic.category.CategoryRepository;
import spring.movieclinic.movie.Movie;
import spring.movieclinic.movie.MovieRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OmdbService {
    private static final String SEARCH_URL = "http://www.omdbapi.com/?s=TITLE&type=movie&apikey=APIKEY";
    private static final String SEARCH_BY_IMDB_URL = "http://www.omdbapi.com/?i=IMDB&apikey=APIKEY";
    private static final String API_KEY = "5ca97d27";
    private final RestTemplate restTemplate;
    private final CategoryRepository categoryRepository;
    private final MovieRepository movieRepository;
    private final List<OmdbMovie> found = new ArrayList<>();

    List<OmdbMovie> findMovies(String title) {
        found.clear();
        String requestURL = SEARCH_URL
                .replaceAll("TITLE", title)
                .replaceAll("APIKEY", API_KEY);
        OmdbMoviesList response = restTemplate.getForObject(requestURL, OmdbMoviesList.class);

        if (response != null) {
            List<OmdbMovie> movies = response.getOmdbMovies();
            for (OmdbMovie movie : movies) {
                requestURL = SEARCH_BY_IMDB_URL
                        .replaceAll("IMDB", movie.getId())
                        .replaceAll("APIKEY", API_KEY);
                ;
                found.add(restTemplate.getForObject(requestURL, OmdbMovie.class));
            }
        }

        return found;
    }

    List<OmdbMovie> saveMovie(String id) {
        for (OmdbMovie movie : found) {
            if (id.equals(movie.getId())) {
                Movie m = new Movie();
                m.setName(movie.getTitle());
                m.setDescription(movie.getPlot());
                m.setYear(movie.getYear());
                m.setPictureURL(movie.getPoster());

                String categories = movie.getGenre();
                String[] genres = categories.split(", ");
                for (String genre : genres) {
                    Optional<Category> optional = categoryRepository.findByName(genre);
                    optional.ifPresent(m::addCategory);
                }

                movieRepository.save(m);
                found.remove(movie);
                break;
            }
        }
        return found;
    }
}
