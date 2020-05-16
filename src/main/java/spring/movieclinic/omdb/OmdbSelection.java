package spring.movieclinic.omdb;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class OmdbSelection {
    @NotEmpty
    List<String> movies;
}
