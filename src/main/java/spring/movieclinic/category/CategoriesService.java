package spring.movieclinic.category;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoriesService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Iterable<Category> categories() {
        if(categoryRepository.findByOrderByNameAsc().isEmpty()) {
            return null;
        }
        return categoryRepository.findByOrderByNameAsc();
    }

    public Category create (Category category) {
        if(categoryRepository.count() > 0) {
            for (Category c : categories()) {
                if(c.equals(category)) {
                    return null;
                }
            }
        }
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
