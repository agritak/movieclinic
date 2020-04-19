package spring.movieclinic.movie;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import spring.movieclinic.category.Category;
import spring.movieclinic.model.ItemEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Indexed
@Table(name = "movies")
public class Movie extends ItemEntity {

    @ManyToMany
    @IndexedEmbedded
    @Analyzer(definition = "movieAnalyzer")
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
