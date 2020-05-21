package spring.movieclinic.omdb;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import spring.movieclinic.category.Category;
import spring.movieclinic.category.CategoryRepository;
import spring.movieclinic.movie.Movie;
import spring.movieclinic.movie.MovieRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public final class OmdbService {
    private final OmdbGateway omdbGateway;
    private final CategoryRepository categoryRepository;
    private final MovieRepository movieRepository;
    private final OmdbConverter omdbConverter;

    List<OmdbOption> findMovies(final String title) {
        List<OmdbOption> list = markExistingOmdbOptions(
                getFullData(omdbGateway.searchBy(title.trim())));
        list.forEach(omdbConverter::toBase64Movie);
        return list;
    }

    void saveMovies(final OmdbSelection form) {
        List<OmdbOption> selected = form
                .getMovies()
                .stream()
                .map(omdbConverter::fromBase64Movie)
                .collect(Collectors.toList());

        selected.forEach(m -> m.setExists(false));
        saveChosen(markExistingOmdbOptions(selected));
    }

    private List<OmdbOption> getFullData(
            final List<OmdbDraft> found) {
        List<OmdbOption> options = new ArrayList<>(found.size());
        found.forEach(draft -> omdbGateway.getBy(draft.getId())
                .ifPresent(movie ->
                        options.add(new OmdbOption(movie))));
        return options;
    }

    private List<OmdbOption> markExistingOmdbOptions(
            final List<OmdbOption> options) {
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

    private void saveChosen(final List<OmdbOption> found) {
        List<Movie> movies = new ArrayList<>();
        for (OmdbOption option : found) {
            if (!option.getExists()) {
                movies.add(option.toMovie(
                        genreToCategories(option.getGenre())));
            }
        }
        movieRepository.saveAll(movies);
    }

    private Set<Category> genreToCategories(final String genre) {
        String[] genres = genre.split(", ");
        List<String> categories = Arrays.asList(genres);
        return categoryRepository.findByNameIn(categories);

    }

}
