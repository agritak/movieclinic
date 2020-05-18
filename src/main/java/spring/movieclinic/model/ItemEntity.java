package spring.movieclinic.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import spring.movieclinic.category.Category;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
public class ItemEntity extends BaseEntity {

    @Column(unique = true)
    @NotBlank(message = "Name is mandatory")
    public String name;

    @NotBlank(message = "Description is mandatory")
    @Size(max = 500, message = "the maximum number of characters is 500")
    private String description;

//    @Column(name = "picture_url")
//    @NotBlank(message = "Picture is mandatory")
//    private String pictureURL;

}
