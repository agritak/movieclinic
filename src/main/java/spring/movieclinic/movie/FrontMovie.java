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
public final class FrontMovie {
    private Integer id;
    @NotBlank
    private String name;
    public static final int TOP = 500;
    @Size(max = TOP, message = "the maximum number of characters is 500")
    private String description;
    private Set<Integer> categories;
    public static final int MIN = 1888;
    public static final int MAX = 2020;
    @NotNull
    @Min(value = MIN, message = "must be equal or greater than 1888")
    @Max(value = MAX, message = "must be equal or less than 2020")
    private Integer year;
    @URL
    private String pictureURL;
    @URL
    private String trailerURL;

    public boolean isNew() {

        return this.id == null;
    }

}
