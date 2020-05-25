package spring.movieclinic.category;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import spring.movieclinic.movie.Movie;
import spring.movieclinic.movie.MoviesService;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class CategoriesController {

    private final CategoriesService categoriesService;

    private final MoviesService moviesService;

    @GetMapping("admin/categories")
    public String index(@RequestParam(defaultValue = "1") Integer page,
                        @RequestParam(defaultValue = "10") Integer size,
                        @RequestParam(defaultValue = "name") String sort,
                        Model model) {
        Page<Category> paging = categoriesService.categories(PageRequest.of(page - 1, size, Sort.by(sort)));
        model.addAttribute("paging", paging);
        return "categories/categories-list";
    }

    @GetMapping("/admin/categories/new")
    public String showCategoryForm(Category category) {
        return "categories/create-category.html";
    }

    @PostMapping("/admin/categories/new")
    public String addCategory(@Valid Category category, BindingResult result) {
        Optional<Category> optional = categoriesService.findByName(category.getName());
        if (StringUtils.hasLength(category.getName()) && category.isNew() && optional.isPresent()) {
            result.rejectValue("name", "duplicate", "Category with this name already exists.");
        }
        if (result.hasErrors()) {
            return "categories/create-category";
        } else {
            categoriesService.create(category);
            return "redirect:/admin/categories";
        }
    }

    @GetMapping("/admin/categories/update/{categoryId}")
    public String showUpdateForm(@PathVariable("categoryId") Integer id, Model model) {
        Category category = categoriesService.findById(id);
        model.addAttribute("category", category);

        return "categories/update-categories";
    }

    @PostMapping("/admin/categories/update/{categoryId}")
    public String updateCategory(@PathVariable("categoryId") Integer id,
                                 @Valid Category category, BindingResult result) {
        Optional<Category> optional = categoriesService.findByName(category.getName());
        if (StringUtils.hasLength(category.getName()) && optional.isPresent()
                && !optional.get().getId().equals(id)) {
            result.rejectValue("name", "duplicate", "Category with this name already exists.");
        }
        if (result.hasErrors()) {
            return "categories/update-categories";
        }
        categoriesService.update(id, category);
        return "redirect:/admin/categories";

    }

    @GetMapping("/admin/categories/delete/{categoryId}")
    public String deleteCategory(@PathVariable("categoryId") Integer id) {
        categoriesService.delete(id);
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/categories/{categoryId}")
    public String showCategory(@PathVariable("categoryId") Integer categoryId,
                               @RequestParam(defaultValue = "1") Integer page,
                               @RequestParam(defaultValue = "5") Integer size,
                               Model model) {
        Category category = categoriesService.findById(categoryId);
        Page<Movie> paging = moviesService.moviesAscByCategoryId(categoryId, PageRequest.of(page - 1, size));
        model.addAttribute("category", category);
        model.addAttribute("paging", paging);
        return "categories/category-details";
    }
}
