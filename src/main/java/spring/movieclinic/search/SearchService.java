package spring.movieclinic.search;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import spring.movieclinic.category.Category;
import spring.movieclinic.model.ItemEntity;
import spring.movieclinic.movie.Movie;
import spring.movieclinic.movie.MovieRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
class SearchService {

    private final MovieRepository movieRepository;
    private final String[] stopWords = new String[]{"a", "an", "and", "as", "at", "be", "by", "if", "in","is",
                                                    "no", "of", "on", "or", "the", "to"};

    List<Movie> getSearchBarResults(String query) {
        return checkQueryIfNotValid(query) ? Collections.emptyList() :
                movieRepository.findByNameContains(query.trim());
    }

    String getQueryToDisplay(String query, Movie movie) {
        return query != null ? query : advancedSearchQuery(movie);
    }

    List<Movie> getUserSearchResults(String query, Movie movie) {
        return query == null ? getAdvancedSearchResults(movie) : getSearchBarResults(query);
    }


    Boolean checkQueryIfNotValid(String query) {
        return query.trim().isEmpty() || checkForStopWords(query.trim()).equals("");
    }

    String checkForStopWords(String trimmedQuery) {
        for(String word:stopWords) {
            if(trimmedQuery.equals(word)) {
                return "";
            }
        }
        return trimmedQuery;
    }

    String advancedSearchQuery(Movie movie) {
        return (!movie.getName().isEmpty() ? "Title: " + movie.getName() + " " : "") +
                (movie.getYear() != null ? "Year: " + movie.getYear() + " " : "") +
                (!movie.getCategories().isEmpty()? "Category: " +
                        movie.getCategories()
                                .stream()
                                .map(ItemEntity::getName)
                                .collect(Collectors.joining(", ")) + " " : "") +
                (!movie.getDescription().isEmpty()? "Description: " + movie.getDescription() : "");
    }

    List<Movie> getAdvancedSearchResults(Movie movie) {
        return checkForEmptySearch(movie) ?
                Collections.emptyList() :
                advancedSearchResultsWhenInputNotEmpty(movie);
    }

    boolean checkForEmptySearch(Movie movie) {
        return checkQueryIfNotValid(movie.getName()) &&
                movie.getYear() == null &&
                movie.getCategories().isEmpty() &&
                checkQueryIfNotValid(movie.getDescription());
    }

    List<Movie> advancedSearchResultsWhenInputNotEmpty(Movie movie) {

        List<Movie> listOfFound = getDerivedQueryMethodResults(movie);

        return listOfFound.isEmpty() ?
                Collections.emptyList() :
                getResultsWhenListOfFoundNotEmpty(movie, listOfFound);
    }

    List<Movie> getDerivedQueryMethodResults(Movie movie) {
        return movieRepository.findByNameAndYearAndCategoriesAndDescription(
                movie.getCategories()
                        .stream()
                        .map(Category::getId)
                        .collect(Collectors.toSet()),
                movie.getName(),
                movie.getYear(),
                movie.getDescription());
    }

    List<Movie> getResultsWhenListOfFoundNotEmpty(Movie movie, List<Movie> listOfFound) {

        return movie.getCategories().isEmpty()  ?
                listOfFound.stream().distinct().collect(Collectors.toList()) :
                getResultsWhenCategoriesNotEmpty(movie, listOfFound);
    }

    List<Movie> getResultsWhenCategoriesNotEmpty(Movie movie, List<Movie> listOfFound) {
        return movie.getCategories().size() == 1 ?
                listOfFound :
                new ArrayList<>(getMostRelevantWhenMultipleCategories(listOfFound).entrySet()
                        .stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (oldValue, newValue) -> oldValue, LinkedHashMap::new)).keySet());
    }

    Map<Movie, Integer> getMostRelevantWhenMultipleCategories(List<Movie> listOfFound) {

        Map<Movie, Integer> mapOfFound = new LinkedHashMap<>();
        Map<Movie, Integer> mostRelevant = new LinkedHashMap<>();

        for (Movie mov:listOfFound) {
            int matchingRequestedCategories = 0;
            mapOfFound.put(mov, !mapOfFound.containsKey(mov) ? ++matchingRequestedCategories : mapOfFound.get(mov) + 1);
        }
        for(Map.Entry<Movie, Integer> entry : mapOfFound.entrySet()) {
            if (entry.getValue() > 1) {
                mostRelevant.put(entry.getKey(), entry.getValue());
            }
        }
        return mostRelevant;
    }
}