package spring.movieclinic.search;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import lombok.RequiredArgsConstructor;
import spring.movieclinic.category.Category;
import spring.movieclinic.movie.Movie;

@Service
@RequiredArgsConstructor
class SearchService {

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private final EntityManager entityManager;
    private FullTextEntityManager fullTextEntityManager;

    @PostConstruct
    public void initializeSearch() {
        try {
            fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
            fullTextEntityManager.createIndexer().startAndWait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    List<Movie> searchMovies(String text) {
        QueryBuilder queryBuilder = fullTextEntityManager
                .getSearchFactory()
                .buildQueryBuilder()
                .forEntity(Movie.class)
                .get();

        org.apache.lucene.search.Query query = queryBuilder
                .keyword()
                .onFields("name", "description", "categories.name")
                .matching(text)
                .createQuery();

        org.hibernate.search.jpa.FullTextQuery jpaQuery
                = fullTextEntityManager.createFullTextQuery(query, Movie.class);

        return jpaQuery.getResultList();
    }

    List<Category> searchCategories(String text) {
        QueryBuilder queryBuilder = fullTextEntityManager
                .getSearchFactory()
                .buildQueryBuilder()
                .forEntity(Category.class)
                .get();

        org.apache.lucene.search.Query query = queryBuilder
                .keyword()
                .onFields("name")
                .matching(text)
                .createQuery();

        org.hibernate.search.jpa.FullTextQuery jpaQuery
                = fullTextEntityManager.createFullTextQuery(query, Category.class);

        return jpaQuery.getResultList();
    }
}
