package spring.movieclinic.movie;

import lombok.Getter;
import lombok.Setter;
import spring.movieclinic.category.Category;
import spring.movieclinic.model.ItemEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "movies")
public class Movie extends ItemEntity {

    @ManyToMany
    @JoinTable(
            name = "movie_category",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories;

    @Column(unique = true)
    private Integer year;

    @Column(name = "trailer_url")
    private String trailerURL;
    //private String director;
    //private List<String> cast;

    public void setCategoriesNew() {
        categories = new HashSet<>();
    }

    public void addCategory(Category category) {
        if (categories == null) {
            categories = new HashSet<>();
        }
        categories.add(category);
    }
}
