package spring.movieclinic.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BaseEntityTest {

    @Test
    void isNew_true() {
        BaseEntity base = new BaseEntity();
        assertThat(base.isNew()).isTrue();
    }

    @Test
    void isNew_false() {
        BaseEntity base = new BaseEntity();
        base.setId(1);
        assertThat(base.isNew()).isFalse();
    }
}
