package spring.movieclinic.search;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import spring.movieclinic.category.CategoriesService;
import spring.movieclinic.movie.Movie;

@Controller
@AllArgsConstructor
class SearchController {

    private final CategoriesService categoriesService;
    private final SearchService searchService;

    @GetMapping("admin/search/search-results")
    String adminSideSearch(@RequestParam(
            value = "search") final String query,
                                final Model model) {
        model.addAttribute("query", query);
        model.addAttribute("searchResults",
                searchService.getSearchBarResults(query));
        return "search/search-results";
    }

    @GetMapping("/search/user-search-results")
    String userSideSearch(@RequestParam(
            value = "search", required = false) final String query,
                                                final Movie movie,
                                                final Model model) {
        model.addAttribute("options", categoriesService.categories());
        model.addAttribute("query",
                searchService.getQueryToDisplay(query, movie));
        model.addAttribute("searchResults",
                searchService.getUserSearchResults(query, movie));
        return "search/user-search-results";
    }

    @GetMapping("/advanced")
    String showAdvancedSearchForm(final Model model) {
        model.addAttribute("options",
                categoriesService.categories());
        model.addAttribute("movie", new Movie());
        return "search/advanced-search";
    }
}
