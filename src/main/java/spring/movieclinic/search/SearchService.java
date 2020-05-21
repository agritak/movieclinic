package spring.movieclinic.search;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import spring.movieclinic.category.Category;
import spring.movieclinic.movie.Movie;
import spring.movieclinic.movie.MovieRepository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
class SearchService {
    private final MovieRepository movieRepository;
    private final String[] stopWords = new String[]
            {"a", "an", "and", "as", "at", "be", "by", "if",
                    "in", "is", "no", "of", "on", "or", "the", "to"};

    List<Movie> getSearchBarResults(final String query) {
        return checkQueryIfNotValid(query) ? Collections.emptyList()
                : movieRepository.findByNameContains(query.trim());
    }

    String getQueryToDisplay(final String query,
                             final Movie movie) {
        return query != null ? query
                : advancedSearchQuery(movie);
    }

    List<Movie> getUserSearchResults(final String query,
                                     final Movie movie) {
        return query == null ? getAdvancedSearchResults(movie)
                : getSearchBarResults(query);
    }


    Boolean checkQueryIfNotValid(final String query) {
        return query.trim().isEmpty()
                || checkForStopWords(query.trim()).equals("");
    }

    String checkForStopWords(final String trimmedQuery) {
        for (String word:stopWords) {
            if (trimmedQuery.equals(word)) {
                return "";
            }
        }
        return trimmedQuery;
    }

    String advancedSearchQuery(final Movie movie) {
        return (!movie.getName().isEmpty() ? "Title: "
                + movie.getName() + " " : "")
                + (movie.getYear() != null ? "Year: "
                + movie.getYear() + " " : "")
                + (!movie.getCategories().isEmpty() ? "Category: "
                + movie.getCategories()
                                .stream()
                                .map(Category::getName)
                                .collect(Collectors.joining(", ")) + " " : "")
                + (!movie.getDescription().isEmpty() ? "Description: "
                        + movie.getDescription() : "");
    }

    List<Movie> getAdvancedSearchResults(final Movie movie) {
        return checkForEmptySearch(movie)
                ? Collections.emptyList()
                : advancedSearchResultsWhenInputNotEmpty(movie);
    }

    boolean checkForEmptySearch(final Movie movie) {
        return checkQueryIfNotValid(movie.getName())
                && movie.getYear() == null
                && movie.getCategories().isEmpty()
                && checkQueryIfNotValid(movie.getDescription());
    }

    List<Movie> advancedSearchResultsWhenInputNotEmpty(
            final Movie movie) {

        List<Movie> listOfFound =
                getDerivedQueryMethodResults(movie);

        return listOfFound.isEmpty()
                ? Collections.emptyList()
                : getResultsWhenListOfFoundNotEmpty(movie, listOfFound);
    }

    List<Movie> getDerivedQueryMethodResults(
            final Movie movie) {
        return movieRepository
                .findByNameAndYearAndCategoriesAndDescription(
                movie.getCategories()
                        .stream()
                        .map(Category::getId)
                        .collect(Collectors.toSet()),
                movie.getName(),
                movie.getYear(),
                movie.getDescription());
    }

    List<Movie> getResultsWhenListOfFoundNotEmpty(
            final Movie movie,
            final List<Movie> listOfFound) {

        return movie.getCategories().isEmpty()
                ? listOfFound.stream().distinct()
                .collect(Collectors.toList())
                : getResultsWhenCategoriesNotEmpty(
                        movie, listOfFound);
    }

    List<Movie> getResultsWhenCategoriesNotEmpty(
                                        final Movie movie,
                                        final List<Movie> listOfFound) {
        return movie.getCategories().size() == 1
                ? listOfFound
                : getMostRelevantWhenMultipleCategories(
                        listOfFound);
    }

    List<Movie> getMostRelevantWhenMultipleCategories(
            final List<Movie> listOfFound) {
        return listOfFound
                .stream()
                .filter(movie ->
                        Collections.frequency(listOfFound, movie) > 1)
                .sorted(Comparator.comparing(movie ->
                        Collections.frequency(listOfFound, movie))
                        .reversed())
                .distinct()
                .collect(Collectors.toList());

    }

}
