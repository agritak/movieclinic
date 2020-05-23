package spring.movieclinic.category;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import spring.movieclinic.movie.Movie;
import spring.movieclinic.movie.MoviesService;

import javax.validation.Valid;

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
        Page<Category> paging = categoriesService.paginateCategories(PageRequest.of(page - 1, size, Sort.by(sort)));
        model.addAttribute("paging", paging);
        return "categories/categories-list";
    }

    @GetMapping("/admin/categories/new")
    public String showCategoryForm(Category category) {
        return "categories/create-category.html";
    }

    @PostMapping("/admin/categories/new")
    public String addCategory(@Valid Category category, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "categories/create-category.html";
        } else {
            categoriesService.create(category);
            model.addAttribute("categories", categoriesService.categories());
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
                                 @Valid Category category, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "categories/update-categories";
        }
        categoriesService.update(id, category);
        model.addAttribute("categories", categoriesService.categories());
        return "redirect:/admin/categories";

    }

    @GetMapping("/admin/categories/delete/{categoryId}")
    public String deleteCategory(@PathVariable("categoryId") Integer id, Model model) {
        categoriesService.delete(id);
        model.addAttribute("categories", categoriesService.categories());
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/categories/{categoryId}")
    public String showCategory(@PathVariable("categoryId") Integer categoryId,
                               @RequestParam(defaultValue = "1") Integer page,
                               @RequestParam(defaultValue = "10") Integer size,
                               Model model) {
        Category category = categoriesService.findById(categoryId);
        Page<Movie> paging = moviesService.paginateAnyMoviesList(PageRequest.of(page - 1, size), category.getMovies());
        model.addAttribute("category", category);
        model.addAttribute("paging", paging);
        return "categories/category-details";
    }

}
