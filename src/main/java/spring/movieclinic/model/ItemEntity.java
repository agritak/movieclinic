package spring.movieclinic.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@MappedSuperclass
public class ItemEntity extends BaseEntity {
    @NotBlank
    private String name;
    @Size(max = 500, message = "Maximum length of description is 500 symbols.")
    private String description;
    @Column(name = "picture_url")
    private String pictureURL;

}
