package spring.movieclinic.movie;

import lombok.Data;
import spring.movieclinic.category.Category;

import javax.validation.constraints.*;
import java.util.Set;

@Data
public class FrontMovie {
    private Integer id;
    @NotBlank
    private String name;
    @Size(max = 500, message = "the maximum number of characters is 500")
    private String description;
    @NotEmpty
    private Set<Category> categories;
    @NotNull
    @Min(value = 1888, message = "must be equal or greater than 1888")
    @Max(value = 2020, message = "must be equal or less than 2020")
    private Integer year;
    private String pictureURL;
    private String trailerURL;

    public boolean isNew() {
        return this.id == null;
    }

}
