package spring.movieclinic.omdb;

import com.fasterxml.jackson.annotation.JsonProperty;
//import com.fasterxml.jackson.databind.PropertyNamingStrategy;
//import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
//TODO propertijus vajadzētu pārsaukt tā, kā viņi nāk no JSON. (izņemot gadījumus kad ir divi lielie burti).
//Tātad, es noprotu, ka Omdb izmanto camel case un katram property ir lielais burts. Javā protams
// propertiji vienmēr sākas ar mazo burtu. Lai to risinātu šo klasi var anotēt ar:
// @JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
//TODO un arī jāpārsauc visi propertiji attiecīgi tā, kā viņi saucās Omdb. Tā rezultātā sanāk,
// ka @JsonProperty anotāciju būs jāizmanto tikai priekš imdbID propertija
public class OMDbMovie { //TODO klasei jāsaucās OmdbMovie
    @JsonProperty("imdbID") //TODO šeit ir ok, var izmantot @JsonProeprty
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
