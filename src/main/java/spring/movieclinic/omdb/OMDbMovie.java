package spring.movieclinic.omdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OMDbMovie {
    @JsonProperty("imdbID")
    private String id;
    @JsonProperty("Title")
    private String name;
    @JsonProperty("Plot")
    private String description;
    @JsonProperty("Year")
    private Integer year;
    @JsonProperty("Genre")
    private String categories;
    @JsonProperty("Poster")
    private String pictureURL;

}
