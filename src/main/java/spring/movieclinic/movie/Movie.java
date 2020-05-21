package spring.movieclinic.movie;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spring.movieclinic.category.Category;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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
