package spring.movieclinic.omdb;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.movieclinic.category.Category;
import spring.movieclinic.movie.Movie;

import java.util.Base64;
import java.util.Set;

@Data
@NoArgsConstructor
public class OmdbOption {
    private String id;
    private String title;
    private String plot;
    private Integer year;
    private String genre;
    private String poster;
    private Boolean exists;
    private String base64Movie;

    public OmdbOption(OmdbMovie omdbMovie) {
        this.setId(omdbMovie.getId());
        this.setTitle(omdbMovie.getTitle());
        this.setPlot(omdbMovie.getPlot());
        this.setYear(omdbMovie.getYear());
        this.setGenre(omdbMovie.getGenre());
        this.setPoster(omdbMovie.getPoster());
        this.setExists(false);
    }

    public Movie toMovie(Set<Category> categories) {
        Movie movie = new Movie();
        movie.setName(title);
        movie.setDescription(plot);
        movie.setYear(year);
        movie.setCategories(categories);
        movie.setPictureURL(poster);
        return movie;
    }

    public void tobase64Movie() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(this);
            this.setBase64Movie(Base64.getEncoder().encodeToString(json.getBytes()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
