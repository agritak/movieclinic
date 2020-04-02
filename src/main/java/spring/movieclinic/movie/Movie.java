package spring.movieclinic.movie;

import lombok.Data;
import spring.movieclinic.category.Category;
import spring.movieclinic.model.ItemEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
@Table(name = "movies")
public class Movie extends ItemEntity {

    @ManyToMany
    @JoinTable(
            name = "movie_category",
            joinColumns = @JoinColumn(name = "movie_id"),
    inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories;
    @NotNull
    private Integer year;
    @Column(name = "trailer_url")
    private String trailerURL;
    //private String director;
    //private List<String> cast;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return Objects.equals(getName().toLowerCase(), movie.getName().toLowerCase()) &&
                Objects.equals(getYear(), movie.getYear());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getYear());
    }

}
