package spring.movieclinic.search;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import spring.movieclinic.movie.Movie;
import spring.movieclinic.movie.MovieRepository;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
class SearchService {

    private final MovieRepository movieRepository;
    private final String[] stopWords = new String[]{"a", "an", "and", "as", "at", "be", "by", "if", "in","is",
            "no", "of", "on", "or", "the", "to"};

    Set<Movie> searchOnSeveralFields(Movie movie) {

        Set<Movie> orderedByRelevance = new LinkedHashSet<>();

        List<Movie> byName = checkQueryIfValid(movie.getName()) ?
                new ArrayList<>() : movieRepository.findByNameContains(movie.getName().trim());

        List<Movie> byDescription = checkQueryIfValid(movie.getDescription()) ?
                new ArrayList<>() : movieRepository.findByDescriptionContains(movie.getDescription().trim());
        getCommonResults(byDescription, byName, orderedByRelevance);

        List<Movie> byYear = movie.getYear() == null ?
                new ArrayList<>() : movieRepository.findByYear(movie.getYear());
        getCommonResults(byYear, byName, orderedByRelevance);
        getCommonResults(byYear, byDescription, orderedByRelevance);

        List<Movie> byCategories = movie.getCategories() == null ?
                new ArrayList<>() :movieRepository.findByCategoriesIn(movie.getCategories());
        getCommonResults(byCategories, byName, orderedByRelevance);
        getCommonResults(byCategories, byDescription, orderedByRelevance);
        getCommonResults(byCategories, byYear, orderedByRelevance);

        orderedByRelevance.addAll(byName);
        orderedByRelevance.addAll(byDescription);
        orderedByRelevance.addAll(byYear);
        orderedByRelevance.addAll(byCategories);

        return orderedByRelevance;
    }

    Boolean checkQueryIfValid(String query) {
        return query.trim().isEmpty() || checkForStopWords(query.trim()).equals("");
    }

    private String checkForStopWords(String trimmedQuery) {
        for(String word:stopWords) {
            if(trimmedQuery.equals(word)) {
                return "";
            }
        }
        return trimmedQuery;
    }

    private void getCommonResults(List<Movie> currentList, List<Movie> previousList, Set<Movie> mostRelevant) {
        if(!currentList.isEmpty() && !previousList.isEmpty()) {
            for (Movie movie:currentList) {
                if(previousList.contains(movie)) {
                    mostRelevant.add(movie);
                }
            }
            currentList.removeAll(mostRelevant);
        }
    }
}