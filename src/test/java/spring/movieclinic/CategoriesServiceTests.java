package spring.movieclinic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareConcurrentModel;
import spring.movieclinic.category.CategoriesService;
import spring.movieclinic.category.Category;
import spring.movieclinic.category.CategoryRepository;
import spring.movieclinic.model.ItemEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;



import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoriesServiceTests {

    @Mock
    private CategoryRepository repository;

    @InjectMocks
    private CategoriesService service;


    @Test
    public void categories() {

        List<Category> expectedCategories = asList(category(1, "action"));
        when(repository.findByOrderByNameAsc()).thenReturn(expectedCategories);

        service.categories();

        verify(repository).findByOrderByNameAsc();

    }

    @Test
     public void paginateCategories() {
        int pageNumber = 0;
        int pageSize = 1;
        Integer id = 1;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Category category = category(1, "action");
        Page<Category> categoryPage = new PageImpl<>(Collections.singletonList(category));
        when(repository.findAll(pageable)).thenReturn(categoryPage);
        service.paginateCategories(pageable);
        Page<Category> categories = repository.findAll(pageable);
        assertEquals(categories.getNumberOfElements(), 1);
   }

    @Test
    public void create() {
        Category category = new Category();

        when(repository.save(category)).thenReturn(category);

        Category actual = service.create(category);

        verify(repository).save(category);

        assertThat(actual).isEqualTo(repository.save(category));

    }

    @Test
    public void update(){
        Integer id = 1;
        Category category = category(id, "action");

         service.update(id, category);

         verify(repository, times(1)).save(eq(category));
    }

    @Test
    public void delete() {
        Integer id = 1;

        service.delete(id);

        verify(repository, times(1)).deleteById(eq(id));

    }

    @Test
    public void findById() {
        Integer id = 1;
        Category category = category(id, "action");

        when(repository.findById(id)).thenReturn(Optional.of(category));
        assertThat(service.findById(id)).isEqualTo(category);

    }

    @Test
    public void findById_idNotFound() {
        Integer id = 1;

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(id))
                        .isEqualToComparingFieldByField(new IllegalArgumentException("Invalid user Id:" + id));

        verify(repository).findById(id);
    }


    @Test
    public void search() {
    String name = "foo";

    List<Category> expectedCategories = asList(category(1, "action"), category(2, "comedy"));

    when(repository.findByNameContains(name)).thenReturn(expectedCategories);

    service.search(name);

    verify(repository).findByNameContains(name);

    }


    private Category category(Integer id, String name) {
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        return category;

    }



}
