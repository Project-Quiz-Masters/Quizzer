package com.example.quizzer.category;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class CategoryRestController {

    private final CategoryService categoryService;

    public CategoryRestController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.listCategories());
    }

    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<Category> getCategoryById(@PathVariable("categoryId") Long categoryId) {
        Optional<Category> c = categoryService.getCategoryById(categoryId);
        return c.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/categories")
    public ResponseEntity<Category> createCategory(@RequestBody Category payload) {
        if (payload == null || payload.getName() == null || payload.getName().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        Category created = categoryService.addCategory(payload.getName(), payload.getDescription());
        URI location = URI.create(String.format("/api/categories/%d", created.getId()));
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/categories/{categoryId}")
    public ResponseEntity<Category> updateCategory(@PathVariable("categoryId") Long categoryId, @RequestBody Category payload) {
        if (!categoryService.existsById(categoryId)) {
            return ResponseEntity.notFound().build();
        }

        Category updated = categoryService.updateCategory(categoryId, payload);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("categoryId") Long categoryId) {
        if (!categoryService.existsById(categoryId)) {
            return ResponseEntity.notFound().build();
        }

        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }
}
