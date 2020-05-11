package spring.movieclinic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.support.BindingAwareConcurrentModel;
import spring.movieclinic.category.CategoriesController;
import spring.movieclinic.category.CategoriesService;
import spring.movieclinic.category.Category;
import spring.movieclinic.category.CategoryRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoriesControllerTests {

    @Mock
    private CategoryRepository repository;

    @Mock
    private CategoriesService service;

    @InjectMocks private CategoriesController categoriesController;


    @Test
    public void index_search() {
        String name = "foo";
        Model model = new BindingAwareConcurrentModel();

        List<Category> expectedCategories = asList(category(1, "action"), category(2, "comedy"));

        when(repository.findByNameContains(name)).thenReturn(expectedCategories);

        String actual = categoriesController.index(name, model);

        assertThat(actual).isEqualTo("categories/categories-list");
        assertThat(model.getAttribute("categories")).isEqualTo(expectedCategories);

        verify(repository).findByNameContains(name);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void index_searchWithEmptyString() {
        String search = "";
        Model model = new BindingAwareConcurrentModel();

        List<Category> expectedCategories = asList(category(1, "action"));
        when(repository.findByOrderByNameAsc()).thenReturn(expectedCategories);

        String actual = categoriesController.index(search, model);
        assertThat(model.getAttribute("categories")).isEqualTo(expectedCategories);

        verify(repository).findByOrderByNameAsc();
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
        Category category = category(id, "action");
        List<Category> expectedCategories = asList(category, category(2, "comedy"));
        Model model = mock(Model.class);
        BindingResult bindingResult = mock(BindingResult.class);

        when(bindingResult.hasErrors()).thenReturn(false);
        when(service.categories()).thenReturn(expectedCategories);
        String actual = categoriesController.addCategory(category, bindingResult, model);

        assertThat(actual).isEqualTo("categories/categories-list");

        verify(bindingResult).hasErrors();
        verify(service).create(category);
        verify(service).categories();
        verifyNoMoreInteractions(service);

        verify(model).addAttribute("categories", expectedCategories);

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

    }

    @Test
    public void updateCategory() {
        Integer id = 1;
        Category category = category(id, "action");
        List<Category> expectedCategories = asList(category, category(2, "comedy"));
        Model model = mock(Model.class);
        BindingResult bindingResult = mock(BindingResult.class);

        when(bindingResult.hasErrors()).thenReturn(false);
        when(service.categories()).thenReturn(expectedCategories);

        String actual = categoriesController.updateCategory(id, category, bindingResult, model);

        assertThat(actual).isEqualTo("categories/categories-list");

        verify(bindingResult).hasErrors();
        verify(service).update(id, category);
        verify(service).categories();
        verifyNoMoreInteractions(service);


        verify(model).addAttribute("categories", expectedCategories);

    }


    @Test
    public void updateCategory_withValidationErrors() {
        Integer id = 1;
        Category category = category(id, "action");
        Model model = mock(Model.class);
        BindingResult bindingResult = mock(BindingResult.class);

        when(bindingResult.hasErrors()).thenReturn(true);

        String actual = categoriesController.updateCategory(id, category, bindingResult, model);

        assertThat(actual).isEqualTo("categories/update-categories");

        verify(bindingResult).hasErrors();
        verifyNoInteractions(repository, model);

    }

    @Test
    public void deleteCategory() {
        Integer id = 1;
        Category category = category(id, "action");
        List<Category> expectedCategories = asList(category, category(2, "comedy"));
        Model model = mock(Model.class);

        when(service.categories()).thenReturn(expectedCategories);

        String actual = categoriesController.deleteCategory(id, model);

        assertThat(actual).isEqualTo("categories/categories-list");

        verify(service).delete(id);
        verify(service).categories();
        verifyNoMoreInteractions(service);

        verify(model).addAttribute("categories", service.categories());

    }

    @Test
    public void showCategory() {
        Model model = new BindingAwareConcurrentModel();
        Integer id = 1;
        Category category = category(id, "action");

        when(service.findById(id)).thenReturn(category);

        String actual = categoriesController.showCategory(id, model);

        assertThat(actual).isEqualTo("categories/category-details");

        assertThat(model.getAttribute("category")).isEqualTo(category);

        verify(service).findById(id);

    }


    private Category category(Integer id, String name) {
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        return category;

    }

}
