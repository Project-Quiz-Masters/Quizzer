package com.example.quizzer.category;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private com.example.quizzer.quiz.QuizRepository quizRepository;

    public Category addCategory(String name, String description) {
        // Check for duplicate category name
        if (categoryRepository.existsByName(name)) {
            throw new IllegalArgumentException(String.format("Category name '%s' is already taken", name));
        }

        Category c = new Category(name, description);
        return categoryRepository.save(c);
    }

    public List<Category> listCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId);
    }

    public void deleteCategory(Long categoryId) {
        // Prevent deleting a category that is assigned to existing quizzes
        if (quizRepository.findByCategoryId(categoryId) != null && !quizRepository.findByCategoryId(categoryId).isEmpty()) {
            throw new IllegalStateException("Cannot delete category: it is assigned to one or more quizzes");
        }

        categoryRepository.deleteById(categoryId);
    }

    /**
     * Update a category. Returns the updated category or null if not found.
     */
    public Category updateCategory(Long categoryId, Category payload) {
        return categoryRepository.findById(categoryId).map(existing -> {
            existing.setName(payload.getName());
            existing.setDescription(payload.getDescription());
            return categoryRepository.save(existing);
        }).orElse(null);
    }

    public boolean existsById(Long categoryId) {
        return categoryRepository.existsById(categoryId);
    }
}
