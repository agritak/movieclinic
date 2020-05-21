package spring.movieclinic.user;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import spring.movieclinic.category.CategoriesService;
import spring.movieclinic.category.Category;
import spring.movieclinic.movie.Movie;
import spring.movieclinic.movie.MoviesService;

import static org.springframework.data.domain.Sort.Direction.DESC;


@Controller
@AllArgsConstructor
@RequestMapping
public final class UserController {
    private final MoviesService moviesService;
    private final CategoriesService categoriesService;

    @GetMapping
    public String index(@RequestParam(
                                defaultValue = "1") final Integer page,
                        @RequestParam(
                                defaultValue = "25") final Integer size,
                        @RequestParam(
                                defaultValue = "year") final String sort,
                                                        final Model model) {
        Page<Movie> paging = moviesService.paginateMovies(
                PageRequest.of(page - 1, size,
                        Sort.by(DESC, sort)));
        model.addAttribute("paging", paging);
        return "user/user-home";
    }

    @GetMapping("/categories")
    public String showCategories(final Model model) {
        model.addAttribute("categories",
                categoriesService.categories());
        return "user/user-categories";
    }

    @GetMapping("/categories/{categoryId}")
    public String showCategory(@PathVariable(
                                    "categoryId") final Integer categoryId,
                               @RequestParam(
                                       defaultValue = "1") final Integer page,
                               @RequestParam(
                                       defaultValue = "20") final Integer size,
                               final Model model) {
        Category category = categoriesService.findById(categoryId);
        Page<Movie> paging = moviesService.paginateAnyMoviesList(
                PageRequest.of(page - 1, size),
                category.sortMoviesByName());
        model.addAttribute("category", category);
        model.addAttribute("paging", paging);
        return "user/user-category";
    }

    @GetMapping("/movie/{id}")
    public String showMovie(@PathVariable("id")
                                final Integer id,
                                final Model model) {
        model.addAttribute("movie",
                moviesService.findMovieById(id));
        return "user/user-movie";
    }
}
