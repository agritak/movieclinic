package spring.movieclinic.movie;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import spring.movieclinic.category.Category;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "movies")
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    @EqualsAndHashCode.Include
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
    @EqualsAndHashCode.Include
    private Integer year;

    @Column(name = "picture_url")
    private String pictureURL;

    @Column(name = "trailer_url")
    private String trailerURL;

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


    public boolean isNew() {
        return this.id == null;
    }

}
