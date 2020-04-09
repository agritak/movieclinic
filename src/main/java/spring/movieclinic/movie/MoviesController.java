package spring.movieclinic.movie;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import spring.movieclinic.category.CategoriesService;
import spring.movieclinic.category.Category;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
@RequestMapping("categories/{categoryId}")
public class MoviesController {
    private final MoviesService moviesService;
    private final CategoriesService categoriesService;

    @GetMapping
    public String showCategory(@PathVariable("categoryId") Integer categoryId, Model model) {
        Category category = categoriesService.findById(categoryId);
        model.addAttribute("category", category);
        return "categories/category-details";
    }

    @GetMapping("movies/new")
    public String showMovieForm(@PathVariable("categoryId") Integer categoryId, Model model) {
        Movie movie = new Movie();
        Category category = categoriesService.findById(categoryId);
        movie.addCategory(category);
        model.addAttribute("movie", movie);
        model.addAttribute("options", categoriesService.categories());
        return "movies/create-update-movie";
    }

    @PostMapping("movies/new")
    public String addMovie(@PathVariable("categoryId") Integer categoryId,
                           @Valid Movie movie,
                           BindingResult result,
                           Model model) {
        Category category = categoriesService.findById(categoryId);
        if (result.hasErrors()) {
            model.addAttribute("movie", movie);
            model.addAttribute("options", categoriesService.categories());
            return "movies/create-update-movie";
        } else {
            moviesService.create(movie);
            model.addAttribute("category", category);
            return "redirect:/categories/{categoryId}";
        }
    }

    @GetMapping("movies/update/{movieId}")
    public String showUpdateForm(@PathVariable("movieId") Integer id, Model model) {
        Movie movie = moviesService.findById(id);
        model.addAttribute("movie", movie);
        model.addAttribute("options", categoriesService.categories());
        return "movies/create-update-movie";
    }

    @PostMapping("movies/update/{movieId}")
    public String updateMovie(@PathVariable("movieId") Integer id,
                              @Valid Movie movie,
                              BindingResult result,
                              Model model) {
        if (result.hasErrors()) {
            model.addAttribute("movie", movie);
            model.addAttribute("options", categoriesService.categories());
            return "movies/create-update-movie";
        }
        moviesService.update(id, movie);
        model.addAttribute("movies", moviesService.movies());
        return "redirect:/categories/{categoryId}";
    }

    @GetMapping("movies/delete/{movieId}")
    public String deleteMovie(@PathVariable("categoryId") Integer categoryId,
                              @PathVariable("movieId") Integer movieId,
                              Model model) {
        moviesService.delete(movieId);
        model.addAttribute("category", categoriesService.findById(categoryId));
        return "redirect:/categories/{categoryId}";
    }
}
