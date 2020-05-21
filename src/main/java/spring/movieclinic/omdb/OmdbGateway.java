package spring.movieclinic.omdb;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;

@Service
@AllArgsConstructor
public final class OmdbGateway {
    private static final String URL =
            "http://www.omdbapi.com/";
    private static final String QUERY_PARAM_I = "i";
    private static final String QUERY_PARAM_S = "s";
    private static final String QUERY_PARAM_TYPE = "type";
    private static final String QUERY_PARAM_TYPE_MOVIE = "movie";
    private static final String QUERY_PARAM_APIKEY = "apikey";
    private static final String API_KEY = "5ca97d27";
    private final RestTemplate restTemplate;

    List<OmdbDraft> searchBy(final String title) {
        UriComponents uriComponents =
                UriComponentsBuilder.fromUriString(URL)
                .queryParam(QUERY_PARAM_S, title)
                .queryParam(QUERY_PARAM_TYPE, QUERY_PARAM_TYPE_MOVIE)
                .queryParam(QUERY_PARAM_APIKEY, API_KEY)
                .build();
        return ofNullable(restTemplate.getForObject(
                uriComponents.toUri(), OmdbDraftsList.class))
                .map(OmdbDraftsList::getOmdbDrafts)
                .orElse(emptyList());
    }

    Optional<OmdbMovie> getBy(final String id) {
        UriComponents uriComponents =
                UriComponentsBuilder.fromUriString(URL)
                .queryParam(QUERY_PARAM_I, id)
                .queryParam(QUERY_PARAM_APIKEY, API_KEY)
                .build();
        return ofNullable(restTemplate.getForObject(
                uriComponents.toUri(), OmdbMovie.class));
    }

}
