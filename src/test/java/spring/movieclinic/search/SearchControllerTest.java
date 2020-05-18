package spring.movieclinic.search;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareConcurrentModel;
import spring.movieclinic.category.CategoriesService;
import spring.movieclinic.category.Category;
import spring.movieclinic.movie.Movie;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchControllerTest {

    @Mock
    private SearchService searchService;
    @Mock private CategoriesService categoriesService;

    @InjectMocks
    private SearchController searchController;


    @Test
    void adminSideSearch() {

        String query = "life";
        Model model = new BindingAwareConcurrentModel();

        List<Movie> expected = asList(
                movie(1, "Life is Beautiful"),
                movie(2, "A Bug's Life")
        );

        when(searchService.getSearchBarResults(query)).thenReturn(expected);

        String actual = searchController.adminSideSearch(query, model);

        assertThat(actual).isEqualTo("search/search-results");
        assertThat(model.getAttribute("searchResults")).isEqualTo(expected);
        verify(searchService).getSearchBarResults(query);
        verifyNoMoreInteractions(searchService);
    }

    @Test
    void userSideSearch_whenQueryEnteredInSearchBar() {

        String query = "life";
        Movie movie = mock(Movie.class);
        Model model = new BindingAwareConcurrentModel();
        List<Movie> expectedMovieList = asList(
                movie(1, "Life is Beautiful"),
                movie(2, "A Bug's Life")
        );
        List<Category> expectedCategoryList = asList(
                category(1, "Adventure"),
                category(2, "Animation")
        );

        when(categoriesService.categories()).thenReturn(expectedCategoryList);
        when(searchService.getQueryToDisplay(query, movie)).thenReturn(query);
        when(searchService.getUserSearchResults(query, movie)).thenReturn(expectedMovieList);

        String actual = searchController.userSideSearch(query, movie, model);

        assertThat(model.getAttribute("options")).isEqualTo(expectedCategoryList);
        assertThat(model.getAttribute("query")).isEqualTo(query);
        assertThat(actual).isEqualTo("search/user-search-results");
        assertThat(model.getAttribute("searchResults")).isEqualTo(expectedMovieList);

        verify(searchService).getUserSearchResults(query, movie);
        verify(searchService).getQueryToDisplay(query, movie);
        verify(categoriesService).categories();
        verifyNoMoreInteractions(searchService);
        verifyNoMoreInteractions(categoriesService);
        verifyNoInteractions(movie);
    }

    @Test
    void userSideSearch_whenQueryEnteredInAdvancedSearch() {

        String advancedQuery = "life";
        Movie movie = mock(Movie.class);
        Model model = new BindingAwareConcurrentModel();
        List<Movie> expectedMovieList = asList(
                movie(1, "Life is Beautiful"),
                movie(2, "A Bug's Life")
        );
        List<Category> expectedCategoryList = asList(
                category(1, "Adventure"),
                category(2, "Animation")
        );

        when(categoriesService.categories()).thenReturn(expectedCategoryList);
        when(searchService.getQueryToDisplay(null, movie)).thenReturn(advancedQuery);
        when(searchService.getUserSearchResults(null, movie)).thenReturn(expectedMovieList);

        String actual = searchController.userSideSearch(null, movie, model);

        assertThat(model.getAttribute("options")).isEqualTo(expectedCategoryList);
        assertThat(model.getAttribute("query")).isEqualTo(advancedQuery);
        assertThat(actual).isEqualTo("search/user-search-results");
        assertThat(model.getAttribute("searchResults")).isEqualTo(expectedMovieList);

        verify(searchService).getUserSearchResults(null, movie);
        verify(searchService).getQueryToDisplay(null, movie);
        verify(categoriesService).categories();
        verifyNoMoreInteractions(searchService);
        verifyNoMoreInteractions(categoriesService);
        verifyNoInteractions(movie);
    }

    @Test
    void showAdvancedSearchForm() {

        Model model = new BindingAwareConcurrentModel();
        List<Category> expectedCategoryList = asList(
                category(1, "Adventure"),
                category(2, "Animation")
        );

        when(categoriesService.categories()).thenReturn(expectedCategoryList);

        String actual = searchController.showAdvancedSearchForm(model);

        assertThat(actual).isEqualTo("search/advanced-search");
        assertThat(model.getAttribute("options")).isEqualTo(expectedCategoryList);
        assertThat(model.getAttribute("movie")).isEqualTo(new Movie());

        verify(categoriesService).categories();
        verifyNoMoreInteractions(categoriesService);
    }

    private Movie movie(Integer id, String name) {
        Movie movie = new Movie();
        movie.setId(id);
        movie.setName(name);
        return movie;
    }

    private Category category(Integer id, String name) {
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        return category;
    }
}