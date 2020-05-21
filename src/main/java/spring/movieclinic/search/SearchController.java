package spring.movieclinic.search;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import spring.movieclinic.category.CategoriesService;
import spring.movieclinic.movie.Movie;
import spring.movieclinic.movie.MoviesService;

@Controller
@AllArgsConstructor
class SearchController {

    private final CategoriesService categoriesService;
    private final SearchService searchService;
    private final MoviesService moviesService;

    @GetMapping("admin/search/search-results")
    String adminSideSearch(@RequestParam(defaultValue = "1") Integer page,
                           @RequestParam(defaultValue = "5") Integer size,
                           @RequestParam(value = "search") String query,
                           Model model) {
        model.addAttribute("query", query);
        model.addAttribute("paging", moviesService.paginateAnyMoviesList(
                PageRequest.of(page - 1, size),
                searchService.getSearchBarResults(query)));
        return "search/search-results";
    }

    @GetMapping("/search/user-search-results")
    String userSideSearch(@RequestParam(value = "search", required = false) String query, Movie movie, Model model) {
        model.addAttribute("options", categoriesService.categories());
        model.addAttribute("query", searchService.getQueryToDisplay(query, movie));
        model.addAttribute("searchResults", searchService.getUserSearchResults(query, movie));
        return "search/user-search-results";
    }

    @GetMapping("/advanced")
    String showAdvancedSearchForm(Model model) {
        model.addAttribute("options", categoriesService.categories());
        model.addAttribute("movie", new Movie());
        return "search/advanced-search";
    }
}
