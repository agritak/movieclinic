package spring.movieclinic.movie;

import lombok.*;
import spring.movieclinic.category.Category;
import spring.movieclinic.model.ItemEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
    @NotNull
    @Column(unique = true)
    private Integer year;
    @Column(name = "trailer_url")
    private String trailerURL;
    //private String director;
    //private List<String> cast;

    Set<Category> getCategoriesInternal() {
        if (this.categories == null) {
            this.categories = new HashSet<>();
        }
        return this.categories;
    }

    void setCategoriesNew() {
        categories = new HashSet<>();
    }

    void addCategory(Category category) {
        if (this.isNew()) {
            setCategoriesNew();
        }
        categories.add(category);
    }

    void removeCategory(Category category) {
        if (this.isNew()) {
            getCategoriesInternal();
        }
        categories.remove(category);
    }

}
