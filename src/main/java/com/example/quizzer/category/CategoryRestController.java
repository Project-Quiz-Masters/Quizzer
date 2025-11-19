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
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@Tag(name = "Categories", description = "Operations for retrieving and managing categories")
public class CategoryRestController {

    private final CategoryService categoryService;

    public CategoryRestController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    @Operation(summary = "List categories", description = "Returns all categories.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categories retrieved successfully")
    })
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.listCategories());
    }

    @Operation(summary = "Get a category", description = "Returns the category with the specified id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<Category> getCategoryById(@PathVariable("categoryId") Long categoryId) {
        Optional<Category> c = categoryService.getCategoryById(categoryId);
        return c.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a category", description = "Creates a new category.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Category created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid category data")
    })
    @PostMapping("/categories")
    public ResponseEntity<Category> createCategory(@RequestBody Category payload) {
        if (payload == null || payload.getName() == null || payload.getName().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        Category created = categoryService.addCategory(payload.getName(), payload.getDescription());
        URI location = URI.create(String.format("/api/categories/%d", created.getId()));
        return ResponseEntity.created(location).body(created);
    }

    @Operation(summary = "Update a category", description = "Updates the category with the specified id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category updated successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @PutMapping("/categories/{categoryId}")
    public ResponseEntity<Category> updateCategory(@PathVariable("categoryId") Long categoryId,
            @RequestBody Category payload) {
        if (!categoryService.existsById(categoryId)) {
            return ResponseEntity.notFound().build();
        }

        Category updated = categoryService.updateCategory(categoryId, payload);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete a category", description = "Deletes the category with the specified id.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("categoryId") Long categoryId) {
        if (!categoryService.existsById(categoryId)) {
            return ResponseEntity.notFound().build();
        }

        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "List quizzes for a category", description = "Returns published quizzes that belong to the specified category.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Quizzes retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/categories/{categoryId}/quizzes")
    public ResponseEntity<List<com.example.quizzer.quiz.Quiz>> getPublishedQuizzesByCategory(
            @PathVariable("categoryId") Long categoryId) {
        if (!categoryService.existsById(categoryId)) {
            return ResponseEntity.notFound().build();
        }

        List<com.example.quizzer.quiz.Quiz> quizzes = categoryService.listPublishedQuizzesByCategory(categoryId);
        return ResponseEntity.ok(quizzes);
    }
}
