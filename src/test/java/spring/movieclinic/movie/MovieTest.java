package spring.movieclinic.movie;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spring.movieclinic.category.Category;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class MovieTest {
    private FrontMovie frontMovie;
    private Set<Category> categories;
    private Category adventure;

    @BeforeEach
    void init() {
        frontMovie = new FrontMovie();
        frontMovie.setName("Into the Wild");
        frontMovie.setYear(2007);
        frontMovie.setDescription("Test");

        adventure = new Category();
        adventure.setName("Adventure");

        categories = new HashSet<>();
        categories.add(adventure);
    }

    @Test
    public void constructor() {
        Movie movie = new Movie(frontMovie, categories);

        assertThat(movie).isEqualToIgnoringGivenFields(frontMovie, "id", "categories");
        assertThat(movie.getId()).isNull();
        assertThat(movie.getCategories()).isEqualTo(categories);
        assertThat(movie.getCategories()).containsExactlyInAnyOrder(adventure);
    }

    @Test
    public void update() {
        Movie movie = new Movie();
        movie.setId(1);
        movie.setName("Catch me if you can");

        movie.update(frontMovie, categories);

        assertThat(movie).isEqualToIgnoringGivenFields(frontMovie, "id", "categories");
        assertThat(movie.getCategories()).isEqualTo(categories);
        assertThat(movie.getId()).isEqualTo(1);
        assertThat(movie.getCategories()).containsExactlyInAnyOrder(adventure);
    }

    @Test
    void isNew_true() {
        Movie movie = new Movie();
        assertThat(movie.isNew()).isTrue();
    }

    @Test
    void isNew_false() {
        Movie movie = new Movie();
        movie.setId(1);
        assertThat(movie.isNew()).isFalse();
    }
}
