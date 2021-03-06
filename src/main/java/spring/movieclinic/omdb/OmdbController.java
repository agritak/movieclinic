package spring.movieclinic.omdb;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import spring.movieclinic.movie.MoviesService;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
@RequestMapping("admin/")
public class OmdbController {
    private final OmdbService omdbService;
    private final MoviesService moviesService;

    @GetMapping("omdb/find")
    public String searchForm() {
        return "omdb/omdb-form";
    }

    @GetMapping("omdb/search")
    public String searchForMovies(@RequestParam String title, Model model) {
        model.addAttribute("movies", omdbService.findMovies(title));
        return "omdb/omdb-search";
    }

    @PostMapping("omdb/save")
    public String saveMovie(@Valid OmdbSelection movies, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "omdb/omdb-form";
        }
        omdbService.saveMovies(movies);
        model.addAttribute("movies", moviesService.getMoviesByNameAsc());
        return "redirect:/admin/movies";
    }
}


