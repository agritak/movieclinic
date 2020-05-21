package spring.movieclinic.omdb;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.util.collections.Sets;
import org.mockito.junit.jupiter.MockitoExtension;
import spring.movieclinic.category.Category;
import spring.movieclinic.category.CategoryRepository;
import spring.movieclinic.movie.Movie;
import spring.movieclinic.movie.MovieRepository;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OmdbServiceTest {
    private final String id = "1ts2";
    private final String title = "Interstellar";
    private final Integer year = 2014;
    private final String base64First = "base64First";

    @Mock
    private OmdbGateway omdbGateway;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private MovieRepository movieRepository;
    @Mock
    private OmdbConverter omdbConverter;

    @InjectMocks
    private OmdbService omdbService;

    @Captor
    private ArgumentCaptor<HashSet<Movie>> captor;

    private OmdbOption option;
    private OmdbMovie omdbMovie;

    @BeforeEach
    void setUp() {
        option = new OmdbOption();
        option.setId(id);
        option.setTitle(title);
        String plot = "Plot";
        option.setPlot(plot);
        option.setYear(year);
        String genre = "Action, Drama, Sci-Fi";
        option.setGenre(genre);

        omdbMovie = new OmdbMovie();
        omdbMovie.setId(id);
        omdbMovie.setTitle(title);
        omdbMovie.setYear(year);
        omdbMovie.setGenre(genre);
        omdbMovie.setPlot(plot);
    }

    @Test
    public void findMovie_movieIsNotInDatabase() {
        String id2 = "12dz";
        OmdbDraft draft = new OmdbDraft();
        OmdbDraft omdbDraft = new OmdbDraft();
        draft.setId(id);
        omdbDraft.setId(id2);

        List<OmdbDraft> drafts = Lists.newArrayList(draft, omdbDraft);

        when(omdbGateway.searchBy(title)).thenReturn(drafts);
        when(omdbGateway.getBy(id)).thenReturn(Optional.of(omdbMovie));
        when(omdbGateway.getBy(id2)).thenReturn(Optional.of(new OmdbMovie()));
        when(omdbConverter.toBase64(any(OmdbOption.class))).thenReturn(Optional.of(base64First));
        when(movieRepository.findByNameInAndYearIn(anyList(), anyList())).thenReturn(Lists.emptyList());

        List<OmdbOption> actual = omdbService.findMovies(title);
        OmdbOption option1 = actual.get(0);
        OmdbOption option2 = actual.get(1);

        assertThat(actual).hasSize(2);
        assertThat(option1.getId()).isEqualTo(id);
        assertThat(option1.getExists()).isFalse();
        assertThat(option1).isEqualToIgnoringGivenFields(option, "exists", "base64Movie");
        assertThat(option1.getBase64Movie()).isEqualTo(base64First);

        assertThat(option2.getExists()).isFalse();
        assertThat(option2).isEqualToIgnoringGivenFields(new OmdbOption(), "exists", "base64Movie");
        assertThat(option2.getBase64Movie()).isEqualTo(base64First);

        verify(omdbGateway).searchBy(title);
        verify(omdbGateway, times(2)).getBy(anyString());
        verify(omdbConverter, times(2)).toBase64(any(OmdbOption.class));
        verify(movieRepository).findByNameInAndYearIn(anyList(), anyList());
        verifyNoMoreInteractions(omdbGateway, omdbConverter, movieRepository);
    }

    @Test
    public void findMovie_movieIsInDatabase() {
        String id2 = "2hj4";
        OmdbDraft omdbDraft = new OmdbDraft();
        omdbDraft.setId(id2);
        OmdbDraft draft = new OmdbDraft();
        draft.setId(id);

        List<OmdbDraft> drafts = Lists.newArrayList(draft, omdbDraft);

        List<String> names = Lists.newArrayList(title, null);
        List<Integer> years = Lists.newArrayList(year, null);

        Movie movie = new Movie();
        movie.setName(title);
        movie.setYear(year);
        List<Movie> found = Collections.singletonList(movie);

        when(omdbGateway.searchBy(title)).thenReturn(drafts);
        when(omdbGateway.getBy(id)).thenReturn(Optional.of(omdbMovie));
        when(omdbGateway.getBy(id2)).thenReturn(Optional.of(new OmdbMovie()));
        when(omdbConverter.toBase64(any(OmdbOption.class))).thenReturn(Optional.of(base64First));
        when(movieRepository.findByNameInAndYearIn(names, years)).thenReturn(found);

        List<OmdbOption> actual = omdbService.findMovies(title);
        OmdbOption option1 = actual.get(0);
        OmdbOption option2 = actual.get(1);

        assertThat(actual).hasSize(2);
        assertThat(option1.getId()).isEqualTo(id);
        assertThat(option1.getExists()).isTrue();
        assertThat(option1.getBase64Movie()).isEqualTo(base64First);
        assertThat(option1).isEqualToIgnoringGivenFields(option, "exists", "base64Movie");

        assertThat(option2.getExists()).isFalse();
        assertThat(option2).isEqualToIgnoringGivenFields(new OmdbOption(), "exists", "base64Movie");
        assertThat(option2.getBase64Movie()).isEqualTo(base64First);

        verify(omdbGateway).searchBy(title);
        verify(omdbGateway).getBy(id);
        verify(omdbGateway).getBy(id2);
        verify(omdbConverter, times(2)).toBase64(any(OmdbOption.class));
        verify(movieRepository).findByNameInAndYearIn(names, years);
        verifyNoMoreInteractions(omdbGateway, omdbConverter, movieRepository);
    }

    @Test
    public void saveMovies() {
        String movieTitle = "The Science of Interstellar";
        Integer movieYear = 2015;

        option.setExists(true);
        option.setBase64Movie(base64First);

        OmdbOption option1 = new OmdbOption();
        option1.setTitle(movieTitle);
        option1.setYear(movieYear);
        option1.setGenre("Documentary");

        OmdbSelection form = new OmdbSelection();
        String base64Second = "base64Second";
        form.setMovies(Lists.newArrayList(base64First, base64Second));

        List<String> names = Lists.newArrayList(title, movieTitle);
        List<Integer> years = Lists.newArrayList(year, movieYear);
        List<Movie> found = Lists.emptyList();

        List<String> categories = Arrays.asList("Action", "Drama", "Sci-Fi");
        Set<Category> expected1 = Sets.newSet(category("Action"), category("Drama"), category("Sci-Fi"));
        List<String> documentary = Lists.newArrayList("Documentary");
        Set<Category> expected2 = Sets.newSet(category("Documentary"));

        when(omdbConverter.fromBase64(base64First, OmdbOption.class)).thenReturn(Optional.of(option));
        when(omdbConverter.fromBase64(base64Second, OmdbOption.class)).thenReturn(Optional.of(option1));
        when(movieRepository.findByNameInAndYearIn(names, years)).thenReturn(found);
        when(categoryRepository.findByNameIn(categories)).thenReturn(expected1);
        when(categoryRepository.findByNameIn(documentary)).thenReturn(expected2);

        omdbService.saveMovies(form);

        verify(movieRepository).saveAll(captor.capture());
        assertThat(captor.getValue().size()).isEqualTo(form.getMovies().size());
        assertThat(captor.getValue().contains(option.toMovie(expected1))).isTrue();
        assertThat(captor.getValue().contains(option1.toMovie(expected2))).isTrue();

        verify(movieRepository).findByNameInAndYearIn(names, years);
        verify(categoryRepository, times(2)).findByNameIn(anyList());
        verify(movieRepository).saveAll(anySet());
        verifyNoMoreInteractions(movieRepository, categoryRepository);
    }

    @Test
    public void saveMovies_movieExist() {
        option.setExists(false);
        option.setBase64Movie(base64First);

        OmdbSelection form = new OmdbSelection();
        form.setMovies(Lists.newArrayList(base64First));

        List<String> names = Lists.newArrayList(title);
        List<Integer> years = Lists.newArrayList(year);

        Movie movie = new Movie();
        movie.setName(title);
        movie.setYear(year);
        List<Movie> found = Collections.singletonList(movie);

        when(omdbConverter.fromBase64(base64First, OmdbOption.class)).thenReturn(Optional.of(option));
        when(movieRepository.findByNameInAndYearIn(names, years)).thenReturn(found);

        omdbService.saveMovies(form);

        verify(movieRepository).saveAll(captor.capture());
        assertThat(captor.getValue().size()).isEqualTo(0);

        verify(movieRepository).findByNameInAndYearIn(names, years);
        verify(movieRepository).saveAll(anySet());
        verifyNoMoreInteractions(movieRepository);
        verifyNoInteractions(categoryRepository);
    }

    private Category category(String name) {
        Category category = new Category();
        category.setName(name);
        return category;
    }
}
