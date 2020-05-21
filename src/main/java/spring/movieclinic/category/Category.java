package spring.movieclinic.category;

import lombok.Getter;
import lombok.Setter;
import spring.movieclinic.movie.Movie;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;


@Setter
@Getter
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Description is mandatory")
    @Size(max = 500, message = "the maximum number of characters is 500")
    private String description;

    @OrderBy("name")
    @ManyToMany(mappedBy = "categories")
    private Set<Movie> movies = new HashSet<>();

    //metodi, kas uztaisa hashset
    //if movies = null; create hashset


    @Override
    public String toString() {
        return this.getName();
    }
}


