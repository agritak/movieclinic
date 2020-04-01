package spring.movieclinic;

import lombok.Data;
import spring.movieclinic.movie.Movie;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    private String name;

    @Size(max = 250, message = ("The maximum length of the description is 250 symbols."))
    private String description;

    @Column(name = "picture_url")
    private String pictureURL;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "category")
    private Set<Movie> movies;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public Set<Movie> getMovies() {
        return movies;
    }

    public void setMovies(Set<Movie> movies) {
        this.movies = movies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id) &&
                Objects.equals(name, category.name)
                // &&
//                Objects.equals(description, category.description) &&
//                Objects.equals(pictureURL, category.pictureURL) &&
//                Objects.equals(movies, category.movies)
                ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
        // return Objects.hash(id, name, description, pictureURL, movies);
    }
}


