package spring.movieclinic.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@Data
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
public class ItemEntity extends BaseEntity {

    @Column(unique = true)
    private String name;

    private String description;

    @Column(name = "picture_url")
    private String pictureURL;
}
