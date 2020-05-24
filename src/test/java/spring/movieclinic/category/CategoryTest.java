package spring.movieclinic.category;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CategoryTest {

    @Test
    void isNew_true() {
        Category category = new Category();
        assertThat(category.isNew()).isTrue();
    }

    @Test
    void isNew_false() {
        Category category = new Category();
        category.setId(1);
        assertThat(category.isNew()).isFalse();
    }
}
