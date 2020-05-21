package spring.movieclinic.movie;

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
import spring.movieclinic.category.CategoriesService;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("admin/movies")
public final class MoviesController {
    private final MoviesService moviesService;
    private final CategoriesService categoriesService;

    @GetMapping
    public String showList(
                        @RequestParam(defaultValue = "1")
                        final Integer page,
                           @RequestParam(defaultValue = "10")
                           final Integer size,
                           @RequestParam(defaultValue = "name")
                        final String sort,
                        final Model model) {
        Page<Movie> paging =
                moviesService.paginateMovies(
                PageRequest.of(page - 1, size, Sort.by(sort)));
        model.addAttribute("paging", paging);
        return "movies/movies-list";
    }

    @GetMapping("/new")
    public String showMovieForm(final Model model) {
        model.addAttribute("frontMovie", new FrontMovie());
        model.addAttribute("options",
                categoriesService.categories());
        return "movies/create-update-movie";
    }

    @PostMapping("/new")
    public String addMovie(
                    @Valid final FrontMovie frontMovie,
                           final BindingResult result,
                           final Model model) {
        if (validate(frontMovie, result)) {
            model.addAttribute("options",
                    categoriesService.categories());
            return "movies/create-update-movie";
        }
        moviesService.create(frontMovie);
        return "redirect:/admin/movies";
    }

    @GetMapping("update/{movieId}")
    public String showUpdateForm(
            @PathVariable("movieId") final Integer id,
                                    final Model model) {
        model.addAttribute("frontMovie",
                moviesService.findMovieById(id));
        model.addAttribute("options",
                categoriesService.categories());
        return "movies/create-update-movie";
    }

    @PostMapping("update/{movieId}")
    public String updateMovie(
            @PathVariable("movieId") final Integer id,
                              @Valid final FrontMovie frontMovie,
                                     final BindingResult result,
                                     final Model model) {
        if (validate(frontMovie, result)) {
            model.addAttribute("options",
                    categoriesService.categories());
            return "movies/create-update-movie";
        }
        moviesService.update(id, frontMovie);
        return "redirect:/admin/movies";
    }

    @GetMapping("/delete/{movieId}")
    public String deleteMovie(
            @PathVariable("movieId") final Integer movieId) {
        moviesService.delete(movieId);
        return "redirect:/admin/movies";
    }

    private boolean validate(
            final FrontMovie frontMovie,
            final BindingResult result) {
        Optional<Movie> optional =
                moviesService.findMovieByNameAndYear(
                        frontMovie.getName(),
                        frontMovie.getYear());
        if ((optional.isPresent()
                && !optional.get().getId()
                .equals(frontMovie.getId()))
                || (frontMovie.isNew() && optional.isPresent())) {
            result.rejectValue("name", "duplicate",
                    "this movie already exists");
        }
        return result.hasErrors();
    }

}
