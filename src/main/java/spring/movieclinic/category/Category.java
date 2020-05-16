package spring.movieclinic.category;

import lombok.Getter;
import lombok.Setter;
import spring.movieclinic.model.ItemEntity;
import spring.movieclinic.movie.Movie;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
@Entity
@Table(name = "categories")
public class Category extends ItemEntity {

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


