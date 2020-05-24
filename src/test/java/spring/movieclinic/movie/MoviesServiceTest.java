package spring.movieclinic.movie;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import spring.movieclinic.category.Category;
import spring.movieclinic.category.CategoryRepository;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MoviesServiceTest {

    private static final Integer ID = 1;

    @Mock
    private MovieRepository movieRepository;
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private MoviesService moviesService;

    @Captor
    private ArgumentCaptor<Movie> captor;

    private FrontMovie frontMovie;
    private FrontMovie updatedFrontMovie;
    private Movie newMovie;
    private Movie movie;

    @BeforeEach
    public void setUp() {
        frontMovie = frontMovie("Movie", 2020);
        updatedFrontMovie = frontMovie("Updated Movie", 2019);
        updatedFrontMovie.setId(ID);

        newMovie = movie("New Movie", 2020);
        movie = movie("Movie", 2010);
        movie.setId(ID);
    }

    @Test
    public void getMoviesByNameAsc() {
        when(movieRepository.findByOrderByNameAsc()).thenReturn(Arrays.asList(movie, newMovie));

        assertThat(moviesService.getMoviesByNameAsc()).containsExactly(movie, newMovie);

        verify(movieRepository).findByOrderByNameAsc();
        verifyNoMoreInteractions(movieRepository);
    }

    @Test
    public void movies() {
        Pageable pageable = mock(Pageable.class);
        List<Movie> list = Collections.emptyList();
        Page<Movie> expected = new PageImpl<>(list);

        when(movieRepository.findAll(pageable)).thenReturn(expected);

        Page<Movie> actual = moviesService.movies(pageable);

        assertThat(actual).isEqualTo(expected);

        verify(movieRepository).findAll(pageable);
        verifyNoMoreInteractions(movieRepository);
    }

    @Test
    public void moviesAscByCategoryId() {
        Pageable pageable = mock(Pageable.class);
        List<Movie> list = Collections.emptyList();
        Page<Movie> expected = new PageImpl<>(list);
        Integer id = 1;

        when(movieRepository.findMoviesByCategoryIdSortByNameAsc(id, pageable)).thenReturn(expected);

        Page<Movie> actual = moviesService.moviesAscByCategoryId(id, pageable);

        assertThat(actual).isEqualTo(expected);

        verify(movieRepository).findMoviesByCategoryIdSortByNameAsc(id, pageable);
        verifyNoMoreInteractions(movieRepository);
    }



    @Test
    public void findMovieById() {
        when(movieRepository.findById(ID)).thenReturn(Optional.of(movie));

        Movie actual = moviesService.findMovieById(ID);

        assertThat(actual).isEqualTo(movie);

        verify(movieRepository).findById(ID);
        verifyNoMoreInteractions(movieRepository);
    }

    @Test
    public void findMovieById_throwsException() {
        when(movieRepository.findById(ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> moviesService.findMovieById(ID))
                .isEqualToComparingFieldByField(new IllegalArgumentException("Invalid movie Id:" + ID));

        verify(movieRepository).findById(ID);
        verifyNoMoreInteractions(movieRepository);
    }

    @Test
    public void create() {
        Category category = new Category();
        category.setId(1);
        category.setName("Adventure");

        Set<Category> categories = new HashSet<>();
        categories.add(category);

        Set<Integer> ids = new HashSet<>();
        ids.add(1);
        frontMovie.setCategories(ids);

        when(categoryRepository.findByIdIn(ids)).thenReturn(categories);

        moviesService.create(frontMovie);

        verify(movieRepository).save(captor.capture());
        assertThat(captor.getValue()).isEqualToIgnoringGivenFields(frontMovie, "categories");
        assertThat(captor.getValue().getCategories()).isEqualTo(categories);

        verify(categoryRepository).findByIdIn(ids);
        verify(movieRepository).save(any(Movie.class));
        verifyNoMoreInteractions(movieRepository, categoryRepository);
    }

    @Test
    public void create_noIdIsPassed() {
        moviesService.create(updatedFrontMovie);

        verify(movieRepository).save(captor.capture());
        assertThat(captor.getValue().getId()).isNull();
        verifyNoMoreInteractions(movieRepository);
    }


    @Test
    public void update() {
        Optional<Movie> expected = Optional.of(movie);
        updatedFrontMovie.setCategories(new HashSet<>());

        when(movieRepository.findById(ID)).thenReturn(expected);
        when(categoryRepository.findByIdIn(updatedFrontMovie.getCategories())).thenReturn(new HashSet<Category>());

        moviesService.update(ID, updatedFrontMovie);

        verify(movieRepository).save(captor.capture());
        assertThat(captor.getValue().getId()).isEqualTo(ID);

        verify(movieRepository).findById(ID);
        verify(movieRepository).save(movie);
        verify(categoryRepository).findByIdIn(new HashSet<>());
        verifyNoMoreInteractions(movieRepository, categoryRepository);
    }

    @Test
    public void delete() {
        moviesService.delete(ID);

        verify(movieRepository).deleteById(ID);
        verifyNoMoreInteractions(movieRepository);
    }

    @Test
    public void findMovieByNameAndYear() {
        String name = movie.getName();
        Integer year = movie.getYear();
        Optional<Movie> expected = Optional.of(movie);

        when(movieRepository.findByNameAndYear(name, year)).thenReturn(expected);

        Optional<Movie> actual = moviesService.findMovieByNameAndYear(name, year);

        assertThat(actual).isEqualTo(expected);

        verify(movieRepository).findByNameAndYear(name, year);
        verifyNoMoreInteractions(movieRepository);
    }

    private Movie movie(String name, Integer year) {
        Movie movie = new Movie();
        movie.setName(name);
        movie.setYear(year);
        return movie;
    }

    private FrontMovie frontMovie(String name, Integer year) {
        FrontMovie frontMovie = new FrontMovie();
        frontMovie.setName(name);
        frontMovie.setYear(year);
        return frontMovie;
    }
}

