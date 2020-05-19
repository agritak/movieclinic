package spring.movieclinic.movie;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FrontMovieTest {

    @Test
    void isNew_true() {
        FrontMovie movie = new FrontMovie();
        assertThat(movie.isNew()).isTrue();
    }

    @Test
    void isNew_false() {
        FrontMovie movie = new FrontMovie();
        movie.setId(1);
        assertThat(movie.isNew()).isFalse();
    }
}
