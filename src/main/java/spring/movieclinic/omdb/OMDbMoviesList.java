package spring.movieclinic.omdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OMDbMoviesList {
    @JsonProperty("Search")
    private List<OMDbMovie> OMDbMovies = new ArrayList<>();
}
