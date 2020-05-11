package spring.movieclinic.category;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.logging.log4j.util.Strings.isBlank;

@Controller
@AllArgsConstructor
public class CategoriesController {

    private final CategoriesService categoriesService;

    private final CategoryRepository repository;

    @GetMapping("/categories")
    public String index(@RequestParam(required = false) String name, Model model) {
        Iterable<Category> categories;
        if (isBlank(name)) {
            categories = repository.findByOrderByNameAsc();
        } else {
            categories = repository.findByNameContains(name);
        }
        model.addAttribute("categories", categories);
        return "categories/categories-list";
    }

    @GetMapping("/categories/new")
    public String showCategoryForm(Category category) {
        return "categories/create-category.html";
    }

    @PostMapping("/categories/new")
    public String addCategory(@Valid Category category, BindingResult result, Model model) {
        if (result.hasErrors()) {
          //  model.addAttribute("category", categoriesService.categories());
            return "categories/create-category.html";
        } else {
            categoriesService.create(category);
            model.addAttribute("categories", categoriesService.categories());
            return "categories/categories-list";
        }
    }


    @GetMapping("/categories/update/{categoryId}")
    public String showUpdateForm(@PathVariable("categoryId") Integer id, Model model) {
//        Category category = repository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        Category category = categoriesService.findById(id);
        model.addAttribute("category", category);
       // model.addAttribute("category", categoriesService.categories());

        return "categories/update-categories";
    }

    @PostMapping("/categories/update/{categoryId}")
    public String updateCategory(@PathVariable("categoryId") Integer id,
                                 @Valid Category category, BindingResult result, Model model) {
        if(result.hasErrors()) {
            return "categories/update-categories";
        }
        categoriesService.update(id, category);
        model.addAttribute("categories", categoriesService.categories());
        return "categories/categories-list";
    }

    @GetMapping("/categories/delete/{categoryId}")
    public String deleteCategory(@PathVariable("categoryId") Integer id, Model model) {
        categoriesService.delete(id);
        model.addAttribute("categories", categoriesService.categories());
        return "categories/categories-list";
    }

    @GetMapping("categories/{categoryId}")
    public String showCategory(@PathVariable("categoryId") Integer categoryId, Model model) {
        model.addAttribute("category", categoriesService.findById(categoryId));
        return "categories/category-details";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
