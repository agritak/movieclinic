package spring.movieclinic.search;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import spring.movieclinic.category.Category;
import spring.movieclinic.movie.Movie;

import java.util.List;

@Controller
@AllArgsConstructor
class SearchController {

    private final SearchService searchService;

    @GetMapping("search/search-results")
    String search(@RequestParam(value = "search") String query, Model model) {

        List<Category> searchResultsCategories = searchService.searchCategories(query);
        model.addAttribute("searchResultsCategories", searchResultsCategories);

        List<Movie> searchResultsMovies = searchService.searchMovies(query);
        model.addAttribute("searchResultsMovies", searchResultsMovies);

        return "search/search-results";

    }
}
