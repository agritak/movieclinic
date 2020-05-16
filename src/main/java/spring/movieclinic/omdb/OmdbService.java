package spring.movieclinic.omdb;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import spring.movieclinic.category.Category;
import spring.movieclinic.category.CategoryRepository;
import spring.movieclinic.movie.Movie;
import spring.movieclinic.movie.MovieRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OmdbService {
    private final OmdbGateway omdbGateway;
    private final CategoryRepository categoryRepository;
    private final MovieRepository movieRepository;

    List<OmdbOption> findMovies(String title) {
        List<OmdbOption> list = markExistingOmdbOptions(getFullData(omdbGateway.searchBy(title.trim())));
        list.forEach(OmdbOption::tobase64Movie);
        return list;
    }

    void saveMovies(OmdbSelection form) {
        List<OmdbOption> selected = form
                .getMovies()
                .stream()
                .map(this::fromBase64)
                .collect(Collectors.toList());

        selected.forEach(m -> m.setExists(false));
        saveChosen(markExistingOmdbOptions(selected));
    }

    private List<OmdbOption> getFullData(List<OmdbDraft> found) {
        List<OmdbOption> options = new ArrayList<>(found.size());
        found.forEach(draft -> omdbGateway.getBy(draft.getId())
                .ifPresent(movie -> options.add(new OmdbOption(movie))));
        return options;
    }

    private List<OmdbOption> markExistingOmdbOptions(List<OmdbOption> options) {
        List<String> names = new ArrayList<>();
        List<Integer> years = new ArrayList<>();
        for (OmdbOption movie : options) {
            names.add(movie.getTitle());
            years.add(movie.getYear());
        }

        List<Movie> found = movieRepository.findByNameInAndYearIn(names, years);

        for (Movie movie : found) {
            for (OmdbOption option : options) {
                if (option.getTitle().equals(movie.getName())
                        && option.getYear().equals(movie.getYear())) {
                    option.setExists(true);
                    break;
                }
            }
        }
        return options;
    }

    private void saveChosen(List<OmdbOption> found) {
        List<Movie> movies = new ArrayList<>();
        for (OmdbOption option : found) {
            if (!option.getExists()) {
                movies.add(option.toMovie(genreToCategories(option.getGenre())));
            }
        }
        movieRepository.saveAll(movies);
    }

    private OmdbOption fromBase64(String base64Movie) {
        ObjectMapper mapper = new ObjectMapper();
        OmdbOption option = new OmdbOption();
        byte[] decodedBytes = Base64.getDecoder().decode(base64Movie);
        String decodedString = new String(decodedBytes);
        try {
            option = mapper.readValue(decodedString, OmdbOption.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return option;
    }

    private Set<Category> genreToCategories(String genre) {
        String[] genres = genre.split(", ");
        List<String> categories = Arrays.asList(genres);
        return categoryRepository.findByNameIn(categories);
    }
}