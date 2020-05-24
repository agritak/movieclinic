package spring.movieclinic.category;

import lombok.Data;
import lombok.EqualsAndHashCode;
import spring.movieclinic.movie.Movie;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;


@Data
@Entity
@Table(name = "categories")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    @NotBlank(message = "Name is mandatory.")
    @EqualsAndHashCode.Include
    private String name;

    @NotBlank(message = "Description is mandatory.")
    @Size(max = 500, message = "The maximum number of characters is 500.")
    private String description;

    @OrderBy("name")
    @ManyToMany(mappedBy = "categories")
    private Set<Movie> movies = new HashSet<>();

    @Override
    public String toString() {
        return this.getName();
    }

    public boolean isNew() {
        return this.id == null;
    }
}


