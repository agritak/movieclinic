package spring.movieclinic.omdb;

import lombok.Data;
import lombok.NoArgsConstructor;
import spring.movieclinic.category.Category;
import spring.movieclinic.movie.Movie;

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
        String plot = omdbMovie.getPlot();
        this.setId(omdbMovie.getId());
        this.setTitle(omdbMovie.getTitle());
        this.setPlot(plot != null && plot.equals("N/A") ? "" : plot);
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

}
