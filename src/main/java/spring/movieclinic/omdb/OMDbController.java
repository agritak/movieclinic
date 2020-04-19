package spring.movieclinic.omdb;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@AllArgsConstructor
public class OMDbController {
    private final OMDbService omdbSearchService;

    @GetMapping("omdb/find")
    public String searchForm(Model model) {
        model.addAttribute("movie", new OMDbMovie());
        return "omdb/omdb-form";
    }

    // /omdb/search?name=
    @GetMapping("omdb/search")
    public String searchForMovies(@RequestParam String name, Model model) {
        model.addAttribute("movie", new OMDbMovie());
        model.addAttribute("movies", omdbSearchService.findMovies(name));
        return "omdb/omdb-search";
    }

    @GetMapping("omdb/save/{id}")
    public String saveMovie(@PathVariable("id") String id, Model model) {
        model.addAttribute("movie", new OMDbMovie());
        model.addAttribute("movies", omdbSearchService.saveMovie(id));
        return "omdb/omdb-search";
    }
}


