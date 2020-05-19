package spring.movieclinic.omdb;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spring.movieclinic.category.Category;
import spring.movieclinic.movie.Movie;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class OmdbOptionTest {
    private OmdbOption option;

    @BeforeEach
    public void setUp() {
        option = new OmdbOption();
        option.setTitle("Interstellar");
        option.setYear(2014);
        option.setExists(false);
    }

    @Test
    public void constructorTest() {
        OmdbMovie omdbMovie = new OmdbMovie();
        omdbMovie.setId("12ht");
        omdbMovie.setTitle("Interstellar");
        omdbMovie.setYear(2014);

        OmdbOption omdbOption = new OmdbOption(omdbMovie);

        assertThat(omdbOption).isEqualToIgnoringGivenFields(omdbMovie, "exists", "base64Movie");
        assertThat(omdbOption.getExists()).isFalse();
        assertThat(omdbOption.getBase64Movie()).isNull();
    }

    @Test
    public void toMovie() {
        Set<Category> categories = new HashSet<>();

        Movie movie = option.toMovie(categories);

        assertThat(movie.getId()).isNull();
        assertThat(movie.getName()).isEqualTo(option.getTitle());
        assertThat(movie.getDescription()).isEqualTo(option.getPlot());
        assertThat(movie.getCategories()).isEqualTo(categories);
        assertThat(movie.getYear()).isEqualTo(option.getYear());
        assertThat(movie.getPictureURL()).isEqualTo(option.getPoster());
        assertThat(movie.getTrailerURL()).isNull();
    }

}
