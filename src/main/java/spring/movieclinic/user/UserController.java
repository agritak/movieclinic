package spring.movieclinic.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import spring.movieclinic.category.CategoriesService;
import spring.movieclinic.movie.MoviesService;


@Controller
@AllArgsConstructor
@RequestMapping("/webpage")
public class UserController {
    private final MoviesService moviesService;
    private final CategoriesService categoriesService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("movies", moviesService.moviesShuffled());
        return "user/user-home";
    }

    @GetMapping("/categories")
    public String showCategories(Model model) {
        model.addAttribute("categories", categoriesService.categories());
        return "user/user-categories";
    }

    @GetMapping("/categories/{categoryId}")
    public String showCategory(@PathVariable("categoryId") Integer categoryId, Model model) {
        model.addAttribute("category", categoriesService.findById(categoryId));
        return "user/user-category";
    }

    @GetMapping("/movie/{id}")
    public String showMovie(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("movie", moviesService.findMovie(id));
        return "user/user-movie";
    }
}
