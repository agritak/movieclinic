package spring.movieclinic.category;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoriesService {

    @Autowired
    private final CategoryRepository categoryRepository;

    public List<Category> categories() {
        return categoryRepository.findByOrderByNameAsc();

    }

    public Page<Category> categories(Pageable pageable) {
        return categoryRepository.findAll(pageable);

    }

    public Category create(Category category) {
        categoryRepository.save(category);
        return category;

    }

    public void update(Integer id, Category category) {
        category.setId(id);
        categoryRepository.save(category);

    }

    public void delete(Integer id) {
        categoryRepository.deleteById(id);

    }

    public Category findById(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id));
    }

    public Optional<Category> findByName(String name) {
        return categoryRepository.findByName(name);
    }

    public Iterable<Category> search(String keyword) {
        return categoryRepository.findByNameContains(keyword);
    }

}
