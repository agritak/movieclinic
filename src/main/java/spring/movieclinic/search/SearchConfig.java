package spring.movieclinic.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

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