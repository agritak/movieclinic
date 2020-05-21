package spring.movieclinic.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareConcurrentModel;
import spring.movieclinic.category.CategoriesService;
import spring.movieclinic.category.Category;
import spring.movieclinic.movie.Movie;
import spring.movieclinic.movie.MoviesService;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.data.domain.Sort.Direction.DESC;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private MoviesService moviesService;
    @Mock
    private CategoriesService categoriesService;

    @InjectMocks
    private UserController userController;

    private Model model;

    @BeforeEach
    void setUp() {
        model = new BindingAwareConcurrentModel();
    }

    @Test
    public void index() {
        int page = 1;
        int size = 20;
        String sort = "year";

        List<Movie> list = Collections.emptyList();
        Page<Movie> paging = new PageImpl<>(list);
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(DESC, sort));

        when(moviesService.paginateMovies(pageable)).thenReturn(paging);

        String actual = userController.index(page, size, sort, model);

        assertThat(actual).isEqualTo("user/user-home");
        assertThat(model.getAttribute("paging")).isEqualTo(paging);

        verify(moviesService).paginateMovies(pageable);
        verifyNoMoreInteractions(moviesService);
    }

    @Test
    public void showCategories() {
        int page = 1;
        int size = 5;
        String sort = "name";

        List<Category> list = Collections.emptyList();
        Page<Category> paging = new PageImpl<>(list);
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sort));

        when(categoriesService.paginateCategories(pageable)).thenReturn(paging);

        String actual = userController.showCategories(page, size, model);

        assertThat(actual).isEqualTo("user/user-categories");
        assertThat(model.getAttribute("paging")).isEqualTo(paging);

        verify(categoriesService).paginateCategories(pageable);
        verifyNoMoreInteractions(categoriesService);
    }

    @Test
    public void showCategory() {
        Integer id = 1;
        int page = 1;
        int size = 20;

        Category category = new Category();

        Pageable pageable = PageRequest.of(page - 1, size);
        List<Movie> list = Collections.emptyList();
        Page<Movie> paging = new PageImpl<>(list);

        when(categoriesService.findById(id)).thenReturn(category);
        when(moviesService.paginateAnyMoviesList(pageable, category.getMovies())).thenReturn(paging);

        String actual = userController.showCategory(id, page, size, model);

        assertThat(actual).isEqualTo("user/user-category");
        assertThat(model.getAttribute("paging")).isEqualTo(paging);
        assertThat(model.getAttribute("category")).isEqualTo(category);

        verify(categoriesService).findById(id);
        verify(moviesService).paginateAnyMoviesList(pageable, category.getMovies());
        verifyNoMoreInteractions(categoriesService, moviesService);
    }

    @Test
    public void showMovie() {
        Integer id = 1;
        Movie movie = new Movie();
        movie.setId(id);

        when(moviesService.findMovieById(id)).thenReturn(movie);

        String actual = userController.showMovie(id, model);

        assertThat(model.getAttribute("movie")).isEqualTo(movie);
        assertThat(actual).isEqualTo("user/user-movie");

        verify(moviesService).findMovieById(id);
        verifyNoMoreInteractions(moviesService);
    }
}
