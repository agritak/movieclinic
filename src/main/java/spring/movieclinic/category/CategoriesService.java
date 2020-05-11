package spring.movieclinic.category;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoriesService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Iterable<Category> categories() {
        return categoryRepository.findByOrderByNameAsc();
    }

    public Category create (Category category) {
        // Set<>
        categoryRepository.save(category);
        return category;

    }

     public void update (Integer id, Category category) {
        category.setId(id);
        categoryRepository.save(category);
        }

    public void delete (Integer id) {
        categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id));
        categoryRepository.deleteById(id);
    }

    public Category findById(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id));
    }

    public Iterable<Category> search(String keyword) {
        return categoryRepository.findByNameContains(keyword);
    }


}
