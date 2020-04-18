package spring.movieclinic.omdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OMDbMoviesList {
    @JsonProperty("Search")
    List<OMDbMovie> OMDbMovies = new ArrayList<>();
}
