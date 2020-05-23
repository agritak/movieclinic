package spring.movieclinic.omdb;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import spring.movieclinic.category.Category;
import spring.movieclinic.category.CategoryRepository;
import spring.movieclinic.movie.Movie;
import spring.movieclinic.movie.MovieRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OmdbService {
    private final OmdbGateway omdbGateway;
    private final CategoryRepository categoryRepository;
    private final MovieRepository movieRepository;
    private final OmdbConverter omdbConverter;

    List<OmdbOption> findMovies(String title) {
        List<OmdbOption> list = markExistingOmdbOptions(getFullData(omdbGateway.searchBy(title.trim())));
        list.forEach(option -> omdbConverter.toBase64(option).ifPresent(option::setBase64Movie));
        return list;
    }

    void saveMovies(OmdbSelection form) {
        List<OmdbOption> selected = form
                .getMovies()
                .stream()
                .map(o -> omdbConverter.fromBase64(o, OmdbOption.class))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        selected.forEach(m -> m.setExists(false));
        saveChosen(markExistingOmdbOptions(selected));
    }

    private List<OmdbOption> getFullData(List<OmdbDraft> found) {
        List<OmdbOption> options = new ArrayList<>(found.size());
        found.forEach(draft -> omdbGateway.getBy(draft.getId())
                .ifPresent(omdbMovie -> options.add(new OmdbOption(omdbMovie))));
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

        for (OmdbOption option : options) {
            for (Movie movie : found) {
                if (movie.getName().equals(option.getTitle())
                        && movie.getYear().equals(option.getYear())) {
                    option.setExists(true);
                    break;
                }
            }
        }
        return options;
    }

    private void saveChosen(List<OmdbOption> found) {
        Set<Movie> movies = new HashSet<>();
        for (OmdbOption option : found) {
            if (!option.getExists()) {
                movies.add(option.toMovie(genreToCategories(option.getGenre())));
            }
        }
        movieRepository.saveAll(movies);
    }

    private Set<Category> genreToCategories(String genre) {
        String[] genres = genre.split(", ");
        List<String> categories = Arrays.asList(genres);
        return categoryRepository.findByNameIn(categories);
    }
}
