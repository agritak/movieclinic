package spring.movieclinic.omdb;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class OmdbGateway {
    private static final String URL = "http://www.omdbapi.com/";
    private static final String QUERY_PARAM_I = "i";
    private static final String QUERY_PARAM_S = "s";
    private static final String QUERY_PARAM_TYPE = "type";
    private static final String QUERY_PARAM_TYPE_MOVIE = "movie";
    private static final String QUERY_PARAM_APIKEY = "apikey";
    private static final String API_KEY = "5ca97d27";
    private final RestTemplate restTemplate;

    List<OmdbMovie> searchBy(String title) {
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(URL)
                .queryParam(QUERY_PARAM_S, title)
                .queryParam(QUERY_PARAM_TYPE, QUERY_PARAM_TYPE_MOVIE)
                .queryParam(QUERY_PARAM_APIKEY, API_KEY)
                .build();
        OmdbMoviesList response = restTemplate.getForObject(uriComponents.toUri(), OmdbMoviesList.class);
        if (response == null) {
            return new ArrayList<>();
        }
        return response.getOmdbMovies();
    }

    OmdbMovie getBy(String id) {
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(URL)
                .queryParam(QUERY_PARAM_I, id)
                .queryParam(QUERY_PARAM_APIKEY, API_KEY)
                .build();
        return restTemplate.getForObject(uriComponents.toUri(), OmdbMovie.class);
    }

}
