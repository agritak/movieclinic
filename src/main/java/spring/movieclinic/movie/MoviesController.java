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
import java.util.Optional;

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
    public String showMovieForm(Model model) {
        model.addAttribute("frontMovie", new FrontMovie());
        model.addAttribute("options", categoriesService.categories());
        return "movies/create-update-movie";
    }

    @PostMapping("/new")
    public String addMovie(@Valid FrontMovie frontMovie,
                           BindingResult result,
                           Model model) {
        if (validation(frontMovie, result)) {
            model.addAttribute("options", categoriesService.categories());
            return "movies/create-update-movie";
        }
        moviesService.create(frontMovie);
        model.addAttribute("movies", moviesService.movies());
        return "movies/movies-list";
    }

    @GetMapping("update/{movieId}")
    public String showUpdateForm(@PathVariable("movieId") Integer id, Model model) {
        model.addAttribute("frontMovie", moviesService.findMovie(id));
        model.addAttribute("options", categoriesService.categories());
        return "movies/create-update-movie";
    }

    @PostMapping("update/{movieId}")
    public String updateMovie(@PathVariable("movieId") Integer id,
                              @Valid FrontMovie frontMovie,
                              BindingResult result,
                              Model model) {
        if (validation(frontMovie, result)) {
            model.addAttribute("options", categoriesService.categories());
            return "movies/create-update-movie";
        }
        moviesService.update(id, frontMovie);
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

    private Boolean validation(FrontMovie frontMovie, BindingResult result) {
        Optional<Movie> optional = moviesService.movieExists(frontMovie.getName(), frontMovie.getYear());
        if ((optional.isPresent() && !optional.get().getId().equals(frontMovie.getId()))
                || (frontMovie.isNew() && optional.isPresent())) {
            result.rejectValue("name", "duplicate", "this movie already exists");
        }
        return result.hasErrors();
    }
}
