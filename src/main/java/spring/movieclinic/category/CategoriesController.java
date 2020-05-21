package spring.movieclinic.category;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import spring.movieclinic.movie.Movie;
import spring.movieclinic.movie.MoviesService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.apache.logging.log4j.util.Strings.isBlank;

@Controller
@AllArgsConstructor
public class CategoriesController {

    private final CategoriesService categoriesService;

    //added with pagination
    private final MoviesService moviesService;

  //  private final CategoryRepository repository;

    @GetMapping("admin/categories")
    public String index(@RequestParam(defaultValue = "1") Integer page,
                        @RequestParam(defaultValue = "10") Integer size,
                        @RequestParam(defaultValue = "name") String sort,
                        Model model) {
                Page<Category> paging = categoriesService.paginateCategories(
                PageRequest.of(page - 1, size, Sort.by(sort)));
        model.addAttribute("paging", paging);
        return "categories/categories-list";
    }

    //public String index(@RequestParam(required = false) String name, Model model) {
    //        Iterable<Category> categories;
    //        if (isBlank(name)) {
    //            categories = repository.findByOrderByNameAsc();
    //        } else {
    //            categories = repository.findByNameContains(name);
    //        }
    //        model.addAttribute("categories", categories);
    //        return "categories/categories-list";
    //    }


    @GetMapping("/admin/categories/new")
    public String showCategoryForm(Category category) {
        return "categories/create-category.html";
    }

    @PostMapping("/admin/categories/new")
    public String addCategory(@Valid Category category, BindingResult result, Model model) {
        if (result.hasErrors()) {
          //  model.addAttribute("category", categoriesService.categories());
            return "categories/create-category.html";
        } else {
            categoriesService.create(category);
            model.addAttribute("categories", categoriesService.categories());
            return "redirect:/admin/categories";
       //     return "categories/categories-list";
        }
    }


    @GetMapping("/admin/categories/update/{categoryId}")
    public String showUpdateForm(@PathVariable("categoryId") Integer id, Model model) {
//        Category category = repository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        Category category = categoriesService.findById(id);
        model.addAttribute("category", category);
       // model.addAttribute("category", categoriesService.categories());

        return "categories/update-categories";
    }

    @PostMapping("/admin/categories/update/{categoryId}")
    public String updateCategory(@PathVariable("categoryId") Integer id,
                                 @Valid Category category, BindingResult result, Model model) {
        if(result.hasErrors()) {
            return "categories/update-categories";
        }
        categoriesService.update(id, category);
        model.addAttribute("categories", categoriesService.categories());
        return "redirect:/admin/categories";
         // return "categories/categories-list";
    }

    @GetMapping("/admin/categories/delete/{categoryId}")
    public String deleteCategory(@PathVariable("categoryId") Integer id, Model model) {
        categoriesService.delete(id);
        model.addAttribute("categories", categoriesService.categories());
     //   return "categories/categories-list";
        return "redirect:/admin/categories";
    }

//    @GetMapping("/categories/delete/{categoryId}")
//    public String deleteCategory(@PathVariable("categoryId") Integer id, Model model) {
//        categoriesService.delete(id);
//        return "redirect:/categories";
//    }

    @GetMapping("/admin/categories/{categoryId}")
    public String showCategory(@PathVariable("categoryId") Integer categoryId,
                               @RequestParam(defaultValue = "1") Integer page,
                               @RequestParam(defaultValue = "10") Integer size,
                               Model model) {
        Category category = categoriesService.findById(categoryId);
        Page<Movie> paging = moviesService.paginateAnyMoviesList(PageRequest.of(page - 1, size), category.sortMoviesByName());
        model.addAttribute("category", category);
        model.addAttribute("paging", paging);
        return "categories/category-details";
    }

//    @GetMapping("categories/{categoryId}")
//    public String showCategory(@PathVariable("categoryId") Integer categoryId, Model model) {
//        model.addAttribute("category", categoriesService.findById(categoryId));
//        return "categories/category-details";
//    }

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public Map<String, String> handleValidationExceptions(
//            MethodArgumentNotValidException ex) {
//        Map<String, String> errors = new HashMap<>();
//        ex.getBindingResult().getAllErrors().forEach((error) -> {
//            String fieldName = ((FieldError) error).getField();
//            String errorMessage = error.getDefaultMessage();
//            errors.put(fieldName, errorMessage);
//        });
//        return errors;
//    }
}
