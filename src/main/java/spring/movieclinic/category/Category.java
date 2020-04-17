package spring.movieclinic.category;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.annotations.Indexed;
import spring.movieclinic.model.ItemEntity;
import spring.movieclinic.movie.Movie;

import javax.persistence.*;
import java.util.Set;

@Setter
@Getter
@Entity
@Indexed
@Table(name = "categories")
public class Category extends ItemEntity {

    @ManyToMany(mappedBy = "categories")
    private Set<Movie> movies;
}


