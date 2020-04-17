package spring.movieclinic.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.StopFilterFactory;
import org.apache.lucene.analysis.snowball.SnowballPorterFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.search.annotations.*;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@EqualsAndHashCode(callSuper = true)
@AnalyzerDef(name = "movieAnalyzer",
        tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
        filters = {@TokenFilterDef(factory = LowerCaseFilterFactory.class),
                @TokenFilterDef(factory = StopFilterFactory.class),
                @TokenFilterDef(factory = SnowballPorterFilterFactory.class,
                        params = {@Parameter(name = "language", value = "English")
                        })
        })
@MappedSuperclass
public class ItemEntity extends BaseEntity {

    @Field
    @Analyzer(definition = "movieAnalyzer")
    @NotBlank
    @Column(unique = true)
    private String name;

    @Field
    @Analyzer(definition = "movieAnalyzer")
    @Size(max = 500, message = "Maximum length of description is 500 symbols.")
    private String description;

    @Column(name = "picture_url")
    private String pictureURL;

}
