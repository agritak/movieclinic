package spring.movieclinic.category;

import lombok.Getter;
import lombok.Setter;
import spring.movieclinic.model.ItemEntity;
import spring.movieclinic.movie.Movie;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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
}


