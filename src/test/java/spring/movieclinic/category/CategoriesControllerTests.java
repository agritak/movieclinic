package spring.movieclinic.category;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.support.BindingAwareConcurrentModel;
import spring.movieclinic.movie.Movie;
import spring.movieclinic.movie.MoviesService;

import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class CategoriesControllerTests {

    @Mock
    private CategoryRepository repository;

    @Mock
    private CategoriesService service;

    @Mock
    MoviesService moviesService;

    @InjectMocks private CategoriesController categoriesController;


    @Test
    public void index_search() {
        Model model = new BindingAwareConcurrentModel();
        int page = 1;
        int size = 10;
        String sort = "foo";

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sort));

        Page<Category> expectedCategories = service.categories(pageable);

        String actual = categoriesController.index(1, 10, sort, model);

        assertThat(actual).isEqualTo("categories/categories-list");
        assertThat(model.getAttribute("paging")).isEqualTo(expectedCategories);

        verifyNoMoreInteractions(repository);
    }

    @Disabled
    @Test
    public void index_searchWithEmptyString() {
        String sort = "";
        Model model = new BindingAwareConcurrentModel();
        int page = 1;
        int size = 10;
//        int pageNumber = 0;
//        int pageSize = 1;

         Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sort));
       //  Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<Category> expectedCategories = service.categories(pageable);

        // List<Category> expectedCategories = asList(category(1, "action"));
        when(repository.findAll(pageable)).thenReturn(expectedCategories);

        String actual = categoriesController.index(1, 10, sort, model);

    //    assertThat(actual).isEqualTo("index");
        assertThat(model.getAttribute("paging")).isEqualTo(expectedCategories);

//        verify(repository).findByOrderByNameAsc();
        verifyNoMoreInteractions(repository);

    }

    @Test
    public void showCategoryForm() {
        Category category = mock(Category.class);

        String actual = categoriesController.showCategoryForm(category);

        assertThat(actual).isEqualTo("categories/create-category.html");
        verifyNoInteractions(category);

    }

    @Test
    public void addCategory() {
        Integer id = 1;
        String action = "action";
        Category category = category(id, action);
        BindingResult bindingResult = mock(BindingResult.class);

        when(service.findByName(action)).thenReturn(Optional.empty());

        String actual = categoriesController.addCategory(category, bindingResult);

        assertThat(actual).isEqualTo("redirect:/admin/categories");

        verify(service).create(category);
        verify(service).findByName(action);
        verifyNoMoreInteractions(service);
    }

    @Test
    public void addCategory_withValidationErrors() {
        Integer id = 1;
        String action = "action";
        Category category = category(id, action);
        BindingResult bindingResult = mock(BindingResult.class);

        when(service.findByName(action)).thenReturn(Optional.empty());
        when(bindingResult.hasErrors()).thenReturn(true);

        String actual = categoriesController.addCategory(category, bindingResult);

        assertThat(actual).isEqualTo("categories/create-category");

        verify(service).findByName(action);
        verify(bindingResult).hasErrors();
        verifyNoMoreInteractions(service, bindingResult);
    }

    @Test
    public void addCategory_categoryExists() {
        String action = "action";
        Category category = category(null, action);

        BindingResult result = mock(BindingResult.class);

        when(service.findByName(action)).thenReturn(Optional.of(category));
        when(result.hasErrors()).thenReturn(true);

        String actual = categoriesController.addCategory(category, result);

        assertThat(actual).isEqualTo("categories/create-category");

        verify(service).findByName(action);
        verify(result).rejectValue("name", "duplicate", "Category with this name already exists.");
        verify(result).hasErrors();
        verifyNoMoreInteractions(result, service);
    }

    @Test
    public void showUpdateForm() {
        Model model = new BindingAwareConcurrentModel();
        Integer id = 1;
        Category category = category(id, "action");

        when(service.findById(id)).thenReturn(category);

        String actual = categoriesController.showUpdateForm(id, model);

        assertThat(actual).isEqualTo("categories/update-categories");
        assertThat(model.getAttribute("category")).isEqualTo(category);

        verify(service).findById(id);
        verifyNoMoreInteractions(service);
    }

    @Test
    public void updateCategory() {
        Integer id = 1;
        String action = "action";
        Category category = category(id, action);
        BindingResult bindingResult = mock(BindingResult.class);

        when(service.findByName(action)).thenReturn(Optional.empty());
        when(bindingResult.hasErrors()).thenReturn(false);

        String actual = categoriesController.updateCategory(id, category, bindingResult);

        assertThat(actual).isEqualTo("redirect:/admin/categories");

        verify(bindingResult).hasErrors();
        verify(service).update(id, category);
        verifyNoMoreInteractions(service);
    }

    @Test
    public void updateCategory_withValidationErrors() {
        Integer id = 1;
        Category category = category(id, "action");
        BindingResult bindingResult = mock(BindingResult.class);

        when(bindingResult.hasErrors()).thenReturn(true);

        String actual = categoriesController.updateCategory(id, category, bindingResult);

        assertThat(actual).isEqualTo("categories/update-categories");

        verify(bindingResult).hasErrors();
        verifyNoInteractions(repository);
    }

    @Test
    public void updateCategory_categoryExists() {
        String action = "action";
        Integer id = 1;
        Category category = category(id, action);

        BindingResult result = mock(BindingResult.class);

        when(service.findByName(action)).thenReturn(Optional.of(category));
        when(result.hasErrors()).thenReturn(true);

        String actual = categoriesController.updateCategory(id, category, result);

        assertThat(actual).isEqualTo("categories/update-categories");

        verify(service).findByName(action);
        verify(result).rejectValue("name", "duplicate", "Category with this name already exists.");
        verify(result).hasErrors();
        verifyNoMoreInteractions(result, service);
    }

    @Test
    public void deleteCategory() {
        Integer id = 1;

        String actual = categoriesController.deleteCategory(id);

        assertThat(actual).isEqualTo("redirect:/admin/categories");

        verify(service).delete(id);
        verifyNoMoreInteractions(service);
    }

    @Test
    public void showCategory() {
        Model model = new BindingAwareConcurrentModel();
        Integer id = 1;
        Category category = category(id, "action");

        when(service.findById(id)).thenReturn(category);
        int page = 1;
        int size = 10;
        Page<Movie> paging = moviesService.moviesAscByCategoryId(id, PageRequest.of(page - 1, size));
        when(moviesService.moviesAscByCategoryId(id, PageRequest.of(page - 1, size))).thenReturn(paging);

        String actual = categoriesController.showCategory(id, 1, 10, model);

        assertThat(actual).isEqualTo("categories/category-details");

        assertThat(model.getAttribute("category")).isEqualTo(category);
        assertThat(Objects.equals(model.getAttribute("paging"), paging));

        verify(service).findById(id);

    }


    private Category category(Integer id, String name) {
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        return category;

    }


}
