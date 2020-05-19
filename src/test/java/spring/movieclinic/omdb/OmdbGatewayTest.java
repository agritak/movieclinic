package spring.movieclinic.omdb;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OmdbGatewayTest {
    private static final String API_KEY = "5ca97d27";
    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private OmdbGateway gateway;

    @Test
    void searchBy() throws URISyntaxException {
        String uri = "http://www.omdbapi.com/?s=title&type=movie&apikey=" + API_KEY;
        OmdbDraftsList expected = new OmdbDraftsList();
        expected.setOmdbDrafts(new ArrayList<>());
        String title = "title";
        when(restTemplate.getForObject(new URI(uri), OmdbDraftsList.class))
                .thenReturn(expected);

        List<OmdbDraft> actual = gateway.searchBy(title);

        assertThat(actual).isEqualTo(expected.getOmdbDrafts());

        verify(restTemplate).getForObject(new URI(uri), OmdbDraftsList.class);
        verifyNoMoreInteractions(restTemplate);
    }

    @Test
    void getBy() throws URISyntaxException {
        String omdbId = "omdbId";
        String uri = "http://www.omdbapi.com/?i=omdbId&apikey=" + API_KEY;
        OmdbMovie movie = new OmdbMovie();
        Optional<OmdbMovie> expected = Optional.of(movie);

        when(restTemplate.getForObject(new URI(uri), OmdbMovie.class))
                .thenReturn(movie);

        Optional<OmdbMovie> actual = gateway.getBy(omdbId);

        assertThat(actual).isEqualTo(expected);

        verify(restTemplate).getForObject(new URI(uri), OmdbMovie.class);
        verifyNoMoreInteractions(restTemplate);
    }
}