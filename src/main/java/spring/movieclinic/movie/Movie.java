package spring.movieclinic.movie;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spring.movieclinic.category.Category;
import spring.movieclinic.model.ItemEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "movies")
@NoArgsConstructor
public class Movie extends ItemEntity {

    @ManyToMany
    @JoinTable(
            name = "movie_category",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    @OrderBy("name")
    private Set<Category> categories = new HashSet<>();
    @Column(unique = true)
    private Integer year;
    @Column(name = "trailer_url")
    private String trailerURL;
    @Column(name = "picture_url")
    private String pictureURL;

    public Movie(FrontMovie frontMovie, Set<Category> categories) {
        update(frontMovie, categories);
    }

    public void update(FrontMovie frontMovie, Set<Category> categories) {
        this.setName(frontMovie.getName());
        this.setDescription(frontMovie.getDescription());
        this.setYear(frontMovie.getYear());
        this.setCategories(categories);
        this.setPictureURL(frontMovie.getPictureURL());
        this.setTrailerURL(frontMovie.getTrailerURL());
    }
}
