package spring.movieclinic.category;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public final class CategoriesService {

    @Autowired
    private final CategoryRepository categoryRepository;

        public List<Category> categories() {

            return categoryRepository.findByOrderByNameAsc();
        }

        public Page<Category> paginateCategories(final Pageable pageable) {

            return categoryRepository.findAll(pageable);
        }

    public Category create(final Category category) {
        categoryRepository.save(category);
        return category;

    }

     public void update(final Integer id, final Category category) {
        category.setId(id);
        categoryRepository.save(category);
        }

    public void delete(final Integer id) {
        categoryRepository.deleteById(id);
    }

    public Category findById(final Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Invalid category Id:" + id));
    }

    public Iterable<Category> search(final String keyword) {

            return categoryRepository.findByNameContains(keyword);
    }

}
