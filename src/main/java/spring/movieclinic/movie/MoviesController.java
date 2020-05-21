package spring.movieclinic.movie;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import spring.movieclinic.category.CategoriesService;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("admin/movies")
public class MoviesController {
    private final MoviesService moviesService;
    private final CategoriesService categoriesService;

    @GetMapping
    public String showList(@RequestParam(defaultValue = "1") Integer page,
                           @RequestParam(defaultValue = "5") Integer size,
                           @RequestParam(defaultValue = "name") String sort,
                           Model model) {
        Page<Movie> paging = moviesService.paginateMovies(PageRequest.of(page - 1, size, Sort.by(sort)));
        model.addAttribute("paging", paging);
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
        if (validate(frontMovie, result)) {
            model.addAttribute("options", categoriesService.categories());
            return "movies/create-update-movie";
        }
        moviesService.create(frontMovie);
        return "redirect:/admin/movies";
    }

    @GetMapping("update/{movieId}")
    public String showUpdateForm(@PathVariable("movieId") Integer id, Model model) {
        model.addAttribute("frontMovie", moviesService.findMovieById(id));
        model.addAttribute("options", categoriesService.categories());
        return "movies/create-update-movie";
    }

    @PostMapping("update/{movieId}")
    public String updateMovie(@PathVariable("movieId") Integer id,
                              @Valid FrontMovie frontMovie,
                              BindingResult result,
                              Model model) {
        if (validate(frontMovie, result)) {
            model.addAttribute("options", categoriesService.categories());
            return "movies/create-update-movie";
        }
        moviesService.update(id, frontMovie);
        return "redirect:/admin/movies";
    }

    @GetMapping("/delete/{movieId}")
    public String deleteMovie(@PathVariable("movieId") Integer movieId) {
        moviesService.delete(movieId);
        return "redirect:/admin/movies";
    }

    private boolean validate(FrontMovie frontMovie, BindingResult result) {
        Optional<Movie> optional = moviesService.findMovieByNameAndYear(frontMovie.getName(), frontMovie.getYear());
        if ((optional.isPresent() && !optional.get().getId().equals(frontMovie.getId()))
                || (frontMovie.isNew() && optional.isPresent())) {
            result.rejectValue("name", "duplicate", "this movie already exists");
        }
        return result.hasErrors();
    }
}
