package spring.movieclinic.search;

import org.apache.naming.java.javaURLContextFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import spring.movieclinic.category.Category;
import spring.movieclinic.movie.Movie;
import spring.movieclinic.movie.MovieRepository;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private SearchService searchService;

    @Test
    void getSearchBarResults_whenQueryIsNotValid() {

        String query = " and";
        List<Movie> expected = Collections.emptyList();

        List<Movie> actual = searchService.getSearchBarResults(query);

        assertThat(actual).isEqualTo(expected);

    }

    @Test
    void getSearchBarResults_whenQueryIsValid() {

        String query = "life";
        List<Movie> expected = asList(
                movie(1, "Life is Beautiful"),
                movie(2, "A Bug's Life"));
        when(movieRepository.findByNameContains(query)).thenReturn(expected);

        List<Movie> actual = searchService.getSearchBarResults(query);

        assertThat(actual).isEqualTo(expected);
        verify(movieRepository).findByNameContains(query);
        verifyNoMoreInteractions(movieRepository);

    }

//    @Test
//    void getQueryToDisplay_whenQueryIsNull() {
//
//        Movie movie = mock(Movie.class);
////        Movie movie = movie(1, "Life is Beautiful");
//        String expectedQueryToDisplay = "Title: life";
//
//        when(searchService.advancedSearchQuery(movie)).thenReturn(expectedQueryToDisplay);
//
//        String actual = searchService.getQueryToDisplay(null, movie);
//
//        assertThat(actual).isEqualTo(expectedQueryToDisplay);
//
////        verifyNoMoreInteractions(movie);
//    }

    @Test
    void getQueryToDisplay_whenQueryIsNotNull() {

        String query = "life";
        Movie movie = mock(Movie.class);

        String actual = searchService.getQueryToDisplay(query, movie);

        assertThat(actual).isEqualTo(query);

        verifyNoMoreInteractions(movie);
    }

//    @Test
//    void getUserSearchResults_whenQueryIsNull() {
//
//        Movie movie = mock(Movie.class);
//
//        List<Movie> expected = asList(
//                movie(1, "Life is Beautiful"),
//                movie(2, "A Bug's Life"));
//
//        when(searchService.getAdvancedSearchResults(movie)).thenReturn(expected);
//
//        List<Movie> actual = searchService.getUserSearchResults(null, movie);
//
//        assertThat(actual).isEqualTo(expected);
//    }

    @Test
    void getUserSearchResults_whenQueryIsNotNull() {

        String query = "life";

        List<Movie> expected = asList(
                movie(1, "Life is Beautiful"),
                movie(2, "A Bug's Life"));

        when(searchService.getSearchBarResults(query)).thenReturn(expected);

        List<Movie> actual = searchService.getUserSearchResults(query, null);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkQueryIfNotValid_whenQueryIsEmpty() {

        assertTrue(() -> searchService.checkQueryIfNotValid(" "));
    }

    @Test
    void checkQueryIfNotValid_whenQueryIsNotEmptyButIsStopWord() {

        assertTrue(() -> searchService.checkQueryIfNotValid("and"));
    }

    @Test
    void checkQueryIfNotValid_whenQueryIsNotEmptyNotStopWord() {

        assertFalse(() -> searchService.checkQueryIfNotValid("it"));
    }

    @Test
    void checkForStopWords_whenQueryIsStopWord() {

        String trimmedQuery = "and";

        String expected = "";

        String actual = searchService.checkForStopWords(trimmedQuery);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkForStopWords_whenQueryIsNotStopWord() {

        String trimmedQuery = "life";

        String actual = searchService.checkForStopWords(trimmedQuery);

        assertThat(actual).isEqualTo(trimmedQuery);
    }

    @Test
    void advancedSearchQuery() {
    }

    @Test
    void getAdvancedSearchResults_whenEmptySearch() {

        Movie movie = movie("", null, Collections.emptySet(), "");

        List<Movie> expected = Collections.emptyList();

        List<Movie> actual = searchService.getAdvancedSearchResults(movie);

        assertThat(actual).isEqualTo(expected);
    }

//    @Test
//    void getAdvancedSearchResults_whenNotEmptySearch() {
//
//        Movie movie = movie("life", null, Collections.emptySet(), "");
//
//        List<Movie> expected = asList(
//                movie(1, "Life is Beautiful"),
//                movie(2, "A Bug's Life"));
//
//        List<Movie> actual = searchService.getAdvancedSearchResults(movie);
//
//        assertThat(actual).isEqualTo(expected);
//    }

    @Test
    void checkForEmptySearch_whenEmptyInputMovie() {

        Movie movie = movie("", null, Collections.emptySet(), "");

        assertTrue(() -> searchService.checkForEmptySearch(movie));
    }

    @Test
    void checkForEmptySearch_whenNotEmptyInputMovie() {

        Movie movie = movie("life", null, Collections.emptySet(), "");

        assertFalse(() -> searchService.checkForEmptySearch(movie));
    }

    @Test
    void advancedSearchResultsWhenInputNotEmpty_whenFoundIsEmpty() {

        Movie movie = movie("asdfghjkl", null, Collections.emptySet(), "");

        List<Movie> expected = Collections.emptyList();

        when(searchService.getDerivedQueryMethodResults(movie)).thenReturn(expected);

        List<Movie> actual = searchService.advancedSearchResultsWhenInputNotEmpty(movie);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void advancedSearchResultsWhenInputNotEmpty_whenFoundIsNotEmpty() {

        Movie movie = movie("life", null, Collections.emptySet(), "");

        List<Movie> expected = asList(
                movie(1, "Life is Beautiful"),
                movie(2, "A Bug's Life"));

        when(searchService.getDerivedQueryMethodResults(movie)).thenReturn(expected);

        List<Movie> actual = searchService.advancedSearchResultsWhenInputNotEmpty(movie);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getDerivedQueryMethodResults() {

        Movie movie = movie("", 1998, Collections.emptySet(), "");

        List<Movie> expected = asList(
                movie(1, "Life is Beautiful"),
                movie(2, "A Bug's Life"));

        when(movieRepository.findByNameAndYearAndCategoriesAndDescription(
                movie.getCategories()
                        .stream()
                        .map(Category::getId)
                        .collect(Collectors.toSet()),
                movie.getName(),
                movie.getYear(),
                movie.getDescription()))
                .thenReturn(expected);

        List<Movie> actual = searchService.getDerivedQueryMethodResults(movie);

        assertThat(actual).isEqualTo(expected);

        verify(movieRepository).findByNameAndYearAndCategoriesAndDescription(
                movie.getCategories()
                        .stream()
                        .map(Category::getId)
                        .collect(Collectors.toSet()),
                movie.getName(),
                movie.getYear(),
                movie.getDescription());
        verifyNoMoreInteractions(movieRepository);
    }

    @Test
    void getResultsWhenListOfFoundNotEmpty_whenEmptyCategories() {

        Movie movie = movie("", null, Collections.emptySet(), "war");

        List<Movie> listOfFound = asList(
                movie(1, "Life is Beautiful"),
                movie(3, "A Bug's Life"));

        List<Movie> actual = searchService.getResultsWhenListOfFoundNotEmpty(movie, listOfFound);

        assertThat(actual).isEqualTo(listOfFound);
    }

//    @Test
//    void getResultsWhenListOfFoundNotEmpty_whenNotEmptyCategories() {
//    }

    @Test
    void getResultsWhenCategoriesNotEmpty_() {
    }

    @Test
    void getMostRelevantWhenMultipleCategories() {
    }

    private Movie movie(Integer id , String name) {
        Movie movie = new Movie();
        movie.setId(id);
        movie.setName(name);
        return movie;
    }

    private Movie movie(String name, Integer year, Set<Category> categories, String description) {
        Movie movie = new Movie();
        movie.setName(name);
        movie.setYear(year);
        movie.setCategories(categories);
        movie.setDescription(description);
        return movie;
    }

    private Category category(Integer id, String name) {
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        return category;
    }
}