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
    @Column(unique = true)
    private String name;

    @Field
    @Analyzer(definition = "movieAnalyzer")
    private String description;

    @Column(name = "picture_url")
    private String pictureURL;

}
