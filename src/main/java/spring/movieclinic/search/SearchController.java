package spring.movieclinic.search;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import spring.movieclinic.category.CategoriesService;
import spring.movieclinic.category.Category;
import spring.movieclinic.movie.Movie;
import spring.movieclinic.movie.MovieRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
@AllArgsConstructor
class SearchController {

    private final MovieRepository movieRepository;
    private final CategoriesService categoriesService;
    private final SearchService searchService;

    @GetMapping("search/search-results")
    String search(@RequestParam(value = "search") String query, Model model) {

        model.addAttribute("query", query);
        if(searchService.checkQueryIfValid(query)) {
            model.addAttribute("searchResultsMovies", new ArrayList<>());
        }else {
            model.addAttribute("searchResultsMovies", movieRepository.findByNameContains(query.trim()));
        }
        return "search/search-results";
    }

    @GetMapping("/webpage/search/user-search-results")
    String userSideSearch(@RequestParam(value = "search", required = false) String query, Movie movie, Model model) {

        model.addAttribute("options", categoriesService.categories());
        model.addAttribute("query", query);
        if(query != null && !query.isEmpty()) {
            if(searchService.checkQueryIfValid(query)){
                model.addAttribute("searchResultsMovies", new ArrayList<>());
            }else {
                model.addAttribute("searchResultsMovies", movieRepository.findByNameContains(query.trim()));
            }
        }else {
            model.addAttribute("searchResultsMovies", searchService.searchOnSeveralFields(movie));
        }
        return "search/user-search-results";
    }

    @GetMapping("/webpage/advanced")
    String showAdvancedSearchForm(Model model) {
        model.addAttribute("options", categoriesService.categories());
        model.addAttribute("movie", new Movie());
        return "search/advanced-search";
    }

}
