package spring.movieclinic.omdb;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import spring.movieclinic.category.CategoryRepository;
import spring.movieclinic.movie.Movie;
import spring.movieclinic.movie.MovieRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OmdbServiceTest {
    private final String id = "1ts2";
    private final String title = "Interstellar";
    private final String plot = "A team of explorers travel through a wormhole in space in an attempt to ensure humanitys survival.";
    private final String genre = "Action, Drama, Sci-Fi";
    private final Integer year = 2014;
    @Mock
    private OmdbGateway omdbGateway;
    //verify(service).doStuff(captor.capture()));
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private MovieRepository movieRepository;
    @Mock
    private OmdbConverter omdbConverter;
    @InjectMocks
    private OmdbService omdbService;
    @Captor
    private ArgumentCaptor<ArrayList<Movie>> captor;
    private OmdbOption option;

    @BeforeEach
    void setUp() {
        option = new OmdbOption();
        option.setId(id);
        option.setTitle(title);
        option.setPlot(plot);
        option.setYear(year);
        option.setGenre(genre);
    }

    @Test
    public void findMovie_movieIsNotInDatabase() {
        OmdbDraft draft = new OmdbDraft();
        draft.setId(id);

        List<OmdbDraft> drafts = Lists.newArrayList(draft);

        OmdbMovie omdbMovie = new OmdbMovie();
        omdbMovie.setId(id);
        omdbMovie.setTitle(title);
        omdbMovie.setYear(year);
        omdbMovie.setGenre(genre);
        omdbMovie.setPlot(plot);

        List<String> names = Lists.newArrayList(title);
        List<Integer> years = Lists.newArrayList(year);
        List<Movie> found = Lists.emptyList();


        when(omdbGateway.searchBy(title)).thenReturn(drafts);
        when(omdbGateway.getBy(id)).thenReturn(Optional.of(omdbMovie));
        doNothing().when(omdbConverter).toBase64Movie(any(OmdbOption.class));
        when(movieRepository.findByNameInAndYearIn(names, years)).thenReturn(found);

        List<OmdbOption> actual = omdbService.findMovies(title);

        OmdbOption complete = actual.get(0);

        assertThat(actual).hasSize(1);
        assertThat(complete.getId()).isEqualTo(id);
        assertThat(complete.getExists()).isFalse();
        assertThat(complete).isEqualToIgnoringGivenFields(option, "exists", "base64Movie");

        verify(omdbGateway).searchBy(title);
        verify(omdbGateway).getBy(id);
        verify(omdbConverter).toBase64Movie(any(OmdbOption.class));
        verify(movieRepository).findByNameInAndYearIn(names, years);
        verifyNoMoreInteractions(omdbGateway, movieRepository);
    }

}
