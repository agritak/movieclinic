package spring.movieclinic.omdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class OmdbMoviesList {
    @JsonProperty("Search")
    private List<OmdbMovie> omdbMovies;
}
