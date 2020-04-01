package spring.movieclinic.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class CategoriesController {

    private final CategoriesService categoriesService;

    @Autowired
    public CategoriesController(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    @GetMapping("/categories")
    public String index(Model model) {
        model.addAttribute("categories", categoriesService.categories());
        return "categories/categories-list";
    }

    @GetMapping("categories/new")
    public String showCategoryForm(Category category) {
        return "categories/create-category";
    }

    @PostMapping("/categories/new")
    public String addCategory(@Valid Category category, BindingResult result, Model model) {
        if(result.hasErrors()) {
            return "categories/create-category.html";
        } else {
            categoriesService.create(category);
            model.addAttribute("categories", categoriesService.categories());
            return "categories/categories-list";
        }
    }

    @GetMapping("/categories/update/{categoryId}")
    public String showUpdateForm(@PathVariable("categoryId") Integer id, Model model) {
        Category category = categoriesService.findById(id);
        model.addAttribute("category", category);
        return "categories/update-categories";
    }

    @PostMapping("/categories/update/{categoryId}")
    public String updateCategory(@PathVariable("categoryId") Integer id,
                                 @Valid Category category, BindingResult result, Model model) {
        if(result.hasErrors()) {
            return "categories/update-category";
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
}
