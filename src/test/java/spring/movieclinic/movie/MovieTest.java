package spring.movieclinic.movie;

import org.junit.jupiter.api.Test;
import spring.movieclinic.category.Category;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class MovieTest {

    @Test
    public void update() {
        FrontMovie frontMovie = new FrontMovie();
        frontMovie.setName("Into the Wild");
        frontMovie.setYear(2007);
        frontMovie.setDescription("Test");

        Category adventure = new Category();
        adventure.setName("Adventure");
        Set<Category> categories = new HashSet<>();
        categories.add(adventure);

        Movie movie = new Movie();
        movie.setId(1);
        movie.setName("Catch me if you can");
        movie.update(frontMovie, categories);

        assertThat(movie).isEqualToIgnoringGivenFields(frontMovie, "id", "categories");
        assertThat(movie.getCategories()).isEqualTo(categories);
        assertThat(movie.getId()).isEqualTo(1);
        assertThat(movie.getCategories()).containsExactlyInAnyOrder(adventure);
    }
}
