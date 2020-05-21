package spring.movieclinic.category;

import lombok.Getter;
import lombok.Setter;
import spring.movieclinic.model.BaseEntity;
import spring.movieclinic.movie.Movie;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
@Entity
@Table(name = "categories")
public final class Category extends BaseEntity {

    @Column(unique = true)
    @NotBlank(message = "Name is mandatory")
    private String name;

    private static final int TOP = 500;

    @NotBlank(message = "Description is mandatory")
    @Size(max = TOP, message = "the maximum number of characters is 500")
    private String description;

    @ManyToMany(mappedBy = "categories")
    private Set<Movie> movies = new HashSet<>();

    //metodi, kas uztaisa hashset
    //if movies = null; create hashset


    @Override
    public String toString() {
        return this.getName();
    }

    public List<Movie> sortMoviesByName() {
        return this.movies.stream()
                .sorted((a, b) -> a.getName().compareToIgnoreCase(b.getName()))
                .collect(Collectors.toList());
    }


}


