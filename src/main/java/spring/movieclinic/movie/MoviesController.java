package spring.movieclinic.movie;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import spring.movieclinic.category.CategoriesService;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
@RequestMapping("/movies")
public class MoviesController {
    private final MoviesService moviesService;
    private final CategoriesService categoriesService;


    @GetMapping
    public String showList(Model model) {
        model.addAttribute("movies", moviesService.movies());
        return "movies/movies-list";
    }


    @GetMapping("/new")
    public String showMovieForm(Movie movie, Model model) {
        model.addAttribute("options", categoriesService.categories());
        return "movies/create-update-movie";
    }

    @PostMapping("/new")
    public String addMovie(@Valid Movie movie,
                           BindingResult result,
                           Model model) {
        if (result.hasErrors()) {
            model.addAttribute("movie", movie);
            model.addAttribute("options", categoriesService.categories());
            return "movies/create-update-movie";
        } else {
            moviesService.create(movie);
            model.addAttribute("movies", moviesService.movies());
            return "movies/movies-list";
        }
    }

    @GetMapping("/{movieId}")
    public String showUpdateForm(@PathVariable("movieId") Integer id, Model model) {
        Movie movie = moviesService.findById(id);
        model.addAttribute("movie", movie);
        model.addAttribute("options", categoriesService.categories());
        return "movies/create-update-movie";
    }

    @PostMapping("/{movieId}")
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
        return "movies/movies-list";
    }

    @GetMapping("/delete/{movieId}")
    public String deleteMovie(@PathVariable("movieId") Integer movieId,
                              Model model) {
        moviesService.delete(movieId);
        model.addAttribute("movies", moviesService.movies());
        return "movies/movies-list";
    }
}
