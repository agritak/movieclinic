package spring.movieclinic.movie;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Objects;

@Data
@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank
    private String title;
    @Size(max = 250, message = ("The maximum length of the description is 250 symbols."))
    private String plot;
    //private Set<Category> categories;
    @Column(name = "movie_year")
    @NotNull
    private Integer year;
    @Column(name = "picture_url")
    private String pictureURL;
    @Column(name = "trailer_url")
    private String trailerURL;
    //private String director;
    //private List<String> cast;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return Objects.equals(getTitle().toLowerCase(), movie.getTitle().toLowerCase()) &&
                Objects.equals(getYear(), movie.getYear());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getYear());
    }

}
