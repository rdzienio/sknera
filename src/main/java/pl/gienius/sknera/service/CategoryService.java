package pl.gienius.sknera.service;

import org.springframework.stereotype.Service;
import pl.gienius.sknera.entity.Category;
import pl.gienius.sknera.repository.CategoryRepository;

import java.util.List;

@Service
public class CategoryService {
    private CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category getCategory(Long categoryId) {
        return categoryRepository.getCategoryById(categoryId);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
