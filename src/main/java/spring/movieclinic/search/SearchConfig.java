package spring.movieclinic.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

//TODO šo klasi principā var dzēst ārā.
// tā vietā jūs vienkārši varat anotēt metodi initializeSearch() ar @PostConstructor anotāciju (SearchService klasē)
// un pašai SearchService klasei arī vajag anotāciju @Service
@EnableAutoConfiguration
@Configuration
class SearchConfig {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Bean
    SearchService searchService() {

        SearchService searchService = new SearchService(entityManagerFactory);
        searchService.initializeSearch();
        return searchService;
    }
}