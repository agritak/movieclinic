package spring.movieclinic.omdb;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.support.BindingAwareConcurrentModel;
import spring.movieclinic.movie.Movie;
import spring.movieclinic.movie.MoviesService;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OmdbControllerTest {
    @Mock
    MoviesService moviesService;
    @Mock
    private OmdbService omdbService;
    @InjectMocks
    private OmdbController omdbController;
    private Model model;

    @BeforeEach
    void setUp() {
        model = new BindingAwareConcurrentModel();
    }

    @Test
    public void searchForm() {
        String expected = "omdb/omdb-form";
        String actual = omdbController.searchForm();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void searchForMovies() {
        String expected = "omdb/omdb-search";
        String title = "title";
        List<OmdbOption> movies = Arrays.asList(omdbOption("Hachi"), omdbOption("Matrix"));

        when(omdbService.findMovies(title)).thenReturn(movies);

        String actual = omdbController.searchForMovies(title, model);

        assertThat(model.getAttribute("movies")).isEqualTo(movies);
        assertThat(actual).isEqualTo(expected);

        verify(omdbService).findMovies(title);
        verifyNoMoreInteractions(omdbService);
    }

    @Test
    public void saveMovie() {
        BindingResult result = mock(BindingResult.class);
        String expected = "redirect:/admin/movies";
        OmdbSelection list = new OmdbSelection();
        List<Movie> movies = Arrays.asList(movie("Hachi"), movie("Seven Pounds"));

        doNothing().when(omdbService).saveMovies(list);
        when(moviesService.getMoviesByNameAsc()).thenReturn(movies);

        String actual = omdbController.saveMovie(list, result, model);

        assertThat(model.getAttribute("movies")).isEqualTo(movies);
        assertThat(actual).isEqualTo(expected);

        verify(omdbService).saveMovies(list);
        verify(moviesService).getMoviesByNameAsc();
        verifyNoMoreInteractions(omdbService, moviesService);
    }

    OmdbOption omdbOption(String title) {
        OmdbOption option = new OmdbOption();
        option.setTitle(title);
        return option;
    }

    Movie movie(String name) {
        Movie movie = new Movie();
        movie.setName(name);
        return movie;
    }
}