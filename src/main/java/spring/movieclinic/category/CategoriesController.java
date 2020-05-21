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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import spring.movieclinic.movie.Movie;
import spring.movieclinic.movie.MoviesService;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
@RequestMapping("admin")
public final class CategoriesController {

    private final CategoriesService categoriesService;

    private final MoviesService moviesService;

    @GetMapping("/categories")
    public String index(@RequestParam(defaultValue = "1")
                            final Integer page,
                        @RequestParam(defaultValue = "10")
                        final Integer size,
                        @RequestParam(defaultValue = "name")
                            final String sort,
                        final Model model) {
                Page<Category> paging =
                        categoriesService.paginateCategories(
                PageRequest.of(page - 1, size, Sort.by(sort)));
        model.addAttribute("paging", paging);
        return "categories/categories-list";
    }

    @GetMapping("/categories/new")
    public String showCategoryForm(final Category
                                               category) {
        return "categories/create-category.html";
    }

    @PostMapping("/categories/new")
    public String addCategory(@Valid final Category category,
                              final BindingResult result,
                              final Model model) {
        if (result.hasErrors()) {
            return "categories/create-category.html";
        } else {
            categoriesService.create(category);
            model.addAttribute("categories",
                    categoriesService.categories());
            return "redirect:/admin/categories";
        }
    }


    @GetMapping("/categories/update/{categoryId}")
    public String showUpdateForm(
            @PathVariable("categoryId") final Integer id,
                                 final Model model) {
        Category category = categoriesService.findById(id);
        model.addAttribute("category", category);

        return "categories/update-categories";
    }

    @PostMapping("/categories/update/{categoryId}")
    public String updateCategory(@PathVariable("categoryId")
                                     final Integer id,
                                 @Valid final Category category,
                                 final BindingResult result,
                                 final Model model) {
        if (result.hasErrors()) {
            return "categories/update-categories";
        }
        categoriesService.update(id, category);
        model.addAttribute("categories",
                categoriesService.categories());
        return "redirect:/admin/categories";
    }

    @GetMapping("/categories/delete/{categoryId}")
    public String deleteCategory(@PathVariable("categoryId")
                                     final Integer id,
                                 final Model model) {
        categoriesService.delete(id);
        model.addAttribute("categories",
                categoriesService.categories());
        return "redirect:/admin/categories";
    }

    @GetMapping("/categories/{categoryId}")
    public String showCategory(
            @PathVariable("categoryId")
                                   final Integer categoryId,
                               @RequestParam(
                                       defaultValue = "1") final Integer page,
                               @RequestParam(
                                       defaultValue = "10") final Integer size,
                               final Model model) {
        Category category =
                categoriesService.findById(categoryId);
        Page<Movie> paging =
                moviesService.paginateAnyMoviesList(
                PageRequest.of(page - 1, size),
                category.sortMoviesByName());
        model.addAttribute("category", category);
        model.addAttribute("paging", paging);
        return "categories/category-details";
    }

}
