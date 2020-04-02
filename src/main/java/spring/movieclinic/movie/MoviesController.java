package spring.movieclinic.movie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/movies")
public class MoviesController {
    private final MoviesService moviesService;

    @Autowired
    public MoviesController(MoviesService moviesService) {
        this.moviesService = moviesService;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("movies", moviesService.movies());
        return "movies/movies-list";
    }

    @GetMapping("/new")
    public String showMovieForm(Movie movie) {
        return "movies/create-update-movie";
    }

    @PostMapping("/new")
    public String addMovie(@Valid Movie movie, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "movies/create-update-movie";
        } else {
            moviesService.create(movie);
            model.addAttribute("movies", moviesService.movies());
            return "movies/movies-list";
        }
    }

    @GetMapping("/update/{movieId}")
    public String showUpdateForm(@PathVariable("movieId") Integer id, Model model) {
        Movie movie = moviesService.findById(id);
        model.addAttribute("movie", movie);
        return "movies/create-update-movie";
    }

    @PostMapping("/update/{movieId}")
    public String updateMovie(@PathVariable("movieId") Integer id,
                              @Valid Movie movie,
                              BindingResult result,
                              Model model) {
        if (result.hasErrors()) {
            return "movies/create-update-movie";
        }
        moviesService.update(id, movie);
        model.addAttribute("movies", moviesService.movies());
        return "movies/movies-list";
    }

    @GetMapping("/delete/{movieId}")
    public String deleteMovie(@PathVariable("movieId") Integer id, Model model) {
        moviesService.delete(id);
        model.addAttribute("movies", moviesService.movies());
        return "movies/movies-list";
    }
}
