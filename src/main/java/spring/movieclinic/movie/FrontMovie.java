package spring.movieclinic.movie;

import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;
import java.util.Set;

@Data
public class FrontMovie {
    private Integer id;
    @NotBlank(message = "Title is mandatory.")
    private String name;
    @Size(max = 500, message = "The maximum number of characters is 500.")
    private String description;
    private Set<Integer> categories;
    @NotNull(message = "Year is mandatory.")
    @Min(value = 1888, message = "Year must be equal or greater than 1888.")
    @Max(value = 2020, message = "Year must be equal or less than 2020.")
    private Integer year;
    @URL
    private String pictureURL;
    @URL
    private String trailerURL;

    public boolean isNew() {
        return this.id == null;
    }

}
