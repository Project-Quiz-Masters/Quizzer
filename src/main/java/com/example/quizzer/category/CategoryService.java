package com.example.quizzer.category;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Category addCategory(String name, String description) {
        Category c = new Category(name, description);
        return categoryRepository.save(c);
    }

    public List<Category> listCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    /**
     * Update a category. Returns the updated category or null if not found.
     */
    public Category updateCategory(Long id, Category payload) {
        return categoryRepository.findById(id).map(existing -> {
            existing.setName(payload.getName());
            existing.setDescription(payload.getDescription());
            return categoryRepository.save(existing);
        }).orElse(null);
    }

    public boolean existsById(Long id) {
        return categoryRepository.existsById(id);
    }
}
