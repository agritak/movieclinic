package spring.movieclinic.movie;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spring.movieclinic.category.Category;
import spring.movieclinic.model.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "movies")
@NoArgsConstructor
public final class Movie extends BaseEntity {

    @Column(unique = true)
    private String name;

    private String description;

    @ManyToMany
    @JoinTable(
            name = "movie_category",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    @OrderBy("name")
    private Set<Category> categories = new HashSet<>();

    @Column(unique = true)
    private Integer year;

    @Column(name = "picture_url")
    private String pictureURL;

    @Column(name = "trailer_url")
    private String trailerURL;

    public Movie(final FrontMovie frontMovie,
                 final Set<Category> categories1) {
        this.categories = categories1;
        update(frontMovie, categories);
    }

    public void update(final FrontMovie frontMovie,
                       final Set<Category> categories1) {
        this.categories = categories1;
        this.setName(frontMovie.getName());
        this.setDescription(frontMovie.getDescription());
        this.setYear(frontMovie.getYear());
        this.setCategories(categories);
        this.setPictureURL(frontMovie.getPictureURL());
        this.setTrailerURL(frontMovie.getTrailerURL());
    }

}
