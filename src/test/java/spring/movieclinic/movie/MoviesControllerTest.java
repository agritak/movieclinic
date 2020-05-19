package spring.movieclinic.movie;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.support.BindingAwareConcurrentModel;
import spring.movieclinic.category.CategoriesService;
import spring.movieclinic.category.Category;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MoviesControllerTest {
    private static final Integer ID = 1;
    @Mock
    MoviesService moviesService;
    @Mock
    CategoriesService categoriesService;
    @InjectMocks
    MoviesController moviesController;
    private List<Category> categories;
    private FrontMovie frontMovie;
    private Model model;

    @BeforeEach
    void init() {
        frontMovie = frontMovie("Movie", 2012);
        model = new BindingAwareConcurrentModel();
        categories = Arrays.asList(
                category("Action"),
                category("Adventure"));
    }

    @Test
    void showList() {
        int page = 1;
        int size = 10;
        String sort = "name";

        List<Movie> list = Collections.emptyList();
        Page<Movie> paging = new PageImpl<>(list);
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sort));

        when(moviesService.paginateMovies(pageable)).thenReturn(paging);

        String actual = moviesController.showList(page, size, sort, model);

        assertThat(actual).isEqualTo("movies/movies-list");
        assertThat(model.getAttribute("paging")).isEqualTo(paging);

        verify(moviesService).paginateMovies(pageable);
        verifyNoMoreInteractions(moviesService);
    }

    @Test
    public void showMovieForm() {
        when(categoriesService.categories()).thenReturn(categories);

        String actual = moviesController.showMovieForm(model);

        assertThat(actual).isEqualTo("movies/create-update-movie");
        assertThat(model.getAttribute("frontMovie")).isEqualTo(new FrontMovie());
        assertThat(model.getAttribute("options")).isEqualTo(categories);

        verify(categoriesService).categories();
        verifyNoMoreInteractions(categoriesService);
    }

    @Test
    public void addMovie() {
        BindingResult result = mock(BindingResult.class);

        when(moviesService.findMovieByNameAndYear(any(String.class), any(Integer.class))).thenReturn(Optional.empty());
        when(result.hasErrors()).thenReturn(false);
        doNothing().when(moviesService).create(frontMovie);

        String actual = moviesController.addMovie(frontMovie, result, model);

        assertThat(actual).isEqualTo("redirect:/admin/movies");

        verify(moviesService).findMovieByNameAndYear(any(String.class), any(Integer.class));
        verify(result).hasErrors();
        verify(moviesService).create(frontMovie);
        verifyNoMoreInteractions(moviesService, result);
    }

    @Test
    public void addMovie_movieExists() {
        Movie movie = movie("Movie", 2012);
        movie.setId(ID);

        BindingResult result = mock(BindingResult.class);

        when(moviesService.findMovieByNameAndYear(any(String.class), any(Integer.class))).thenReturn(Optional.of(movie));
        when(result.hasErrors()).thenReturn(true);
        //doNothing().when(result).rejectValue("name", "duplicate", "this movie already exists");
        when(categoriesService.categories()).thenReturn(categories);

        String actual = moviesController.addMovie(frontMovie, result, model);

        assertThat(actual).isEqualTo("movies/create-update-movie");
        assertThat(model.getAttribute("options")).isEqualTo(categories);

        verify(moviesService).findMovieByNameAndYear(any(String.class), any(Integer.class));
        verify(result).rejectValue("name", "duplicate", "this movie already exists");
        verify(result).hasErrors();
        verify(categoriesService).categories();
        verifyNoMoreInteractions(result, categoriesService);
    }

    @Test
    public void addMovie_hasFieldErrors() {
        BindingResult result = mock(BindingResult.class);

        when(moviesService.findMovieByNameAndYear(any(String.class), any(Integer.class))).thenReturn(Optional.empty());
        when(result.hasErrors()).thenReturn(true);
        when(categoriesService.categories()).thenReturn(categories);

        String actual = moviesController.addMovie(frontMovie, result, model);

        assertThat(actual).isEqualTo("movies/create-update-movie");
        assertThat(model.getAttribute("options")).isEqualTo(categories);

        verify(moviesService).findMovieByNameAndYear(any(String.class), any(Integer.class));
        verify(result).hasErrors();
        verify(categoriesService).categories();
        verifyNoMoreInteractions(result, categoriesService);
    }

    @Test
    public void showUpdateForm() {
        Movie movie = movie("Movie", 2020);

        when(moviesService.findMovieById(ID)).thenReturn(movie);
        when(categoriesService.categories()).thenReturn(categories);

        String actual = moviesController.showUpdateForm(ID, model);

        assertThat(actual).isEqualTo("movies/create-update-movie");
        assertThat(model.getAttribute("frontMovie")).isEqualTo(movie);
        assertThat(model.getAttribute("options")).isEqualTo(categories);

        verify(moviesService).findMovieById(ID);
        verify(categoriesService).categories();
        verifyNoMoreInteractions(moviesService, categoriesService);
    }

    @Test
    public void updateMovie() {
        BindingResult result = mock(BindingResult.class);

        when(moviesService.findMovieByNameAndYear(any(String.class), any(Integer.class))).thenReturn(Optional.empty());
        when(result.hasErrors()).thenReturn(false);
        doNothing().when(moviesService).update(ID, frontMovie);

        String actual = moviesController.updateMovie(ID, frontMovie, result, model);

        assertThat(actual).isEqualTo("redirect:/admin/movies");

        verify(moviesService).findMovieByNameAndYear(any(String.class), any(Integer.class));
        verify(result).hasErrors();
        verify(moviesService).update(ID, frontMovie);
        verifyNoMoreInteractions(moviesService, result);
    }

    @Test
    public void updateMovie_movieExists() {
        frontMovie.setId(ID);
        Movie movie = movie("Movie", 2012);
        movie.setId(2);

        BindingResult result = mock(BindingResult.class);

        when(moviesService.findMovieByNameAndYear(any(String.class), any(Integer.class))).thenReturn(Optional.of(movie));
        //doNothing().when(result).rejectValue("name", "duplicate", "this movie already exists");
        when(result.hasErrors()).thenReturn(true);
        when(categoriesService.categories()).thenReturn(categories);

        String actual = moviesController.updateMovie(ID, frontMovie, result, model);

        assertThat(actual).isEqualTo("movies/create-update-movie");
        assertThat(model.getAttribute("options")).isEqualTo(categories);

        verify(moviesService).findMovieByNameAndYear(any(String.class), any(Integer.class));
        verify(result).rejectValue("name", "duplicate", "this movie already exists");
        verify(result).hasErrors();
        verify(categoriesService).categories();
        verifyNoMoreInteractions(moviesService, result, categoriesService);
    }

    @Test
    void deleteMovie() {
        //doNothing().when(moviesService).delete(ID);

        String actual = moviesController.deleteMovie(ID);

        assertThat(actual).isEqualTo("redirect:/admin/movies");

        verify(moviesService).delete(ID);
        verifyNoMoreInteractions(moviesService);
    }

    private Movie movie(String name, Integer year) {
        Movie movie = new Movie();
        movie.setName(name);
        movie.setYear(year);
        return movie;
    }

    private FrontMovie frontMovie(String name, Integer year) {
        FrontMovie movie = new FrontMovie();
        movie.setName(name);
        movie.setYear(year);
        return movie;
    }

    private Category category(String name) {
        Category category = new Category();
        category.setName(name);
        return category;
    }

}
