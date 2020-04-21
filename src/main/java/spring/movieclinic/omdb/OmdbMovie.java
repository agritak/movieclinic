package spring.movieclinic.omdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class OmdbMovie {
    @JsonProperty("imdbID")
    private String id;
    private String title;
    private String plot;
    private Integer year;
    private String genre;
    private String poster;

}
