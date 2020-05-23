package spring.movieclinic.movie;

import lombok.Data;
import org.hibernate.validator.constraints.URL;


import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class FrontMovie {
    private Integer id;
    @NotBlank
    private String name;
    @Size(max = 500, message = "the maximum number of characters is 500")
    private String description;
    private Set<Integer> categories;
    @NotNull
    @Min(value = 1888, message = "must be equal or greater than 1888")
    @Max(value = 2020, message = "must be equal or less than 2020")
    private Integer year;
    @URL
    private String pictureURL;
    @URL
    private String trailerURL;

    public boolean isNew() {
        return this.id == null;
    }

}
