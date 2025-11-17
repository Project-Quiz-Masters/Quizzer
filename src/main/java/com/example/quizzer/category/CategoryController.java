package com.example.quizzer.category;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.listCategories());
        return "categories-list";
    }

    @GetMapping("/new")
    public String showAddForm() {
        return "add-category";
    }

    @PostMapping
    public String addCategory(@RequestParam String name, @RequestParam String description) {
        categoryService.addCategory(name, description);
        return "redirect:/categories";
    }

    @PostMapping("/{categoryId}/delete")
    public String deleteCategory(@PathVariable("categoryId") Long categoryId, Model model) {
        try {
            categoryService.deleteCategory(categoryId);
            return "redirect:/categories";
        } catch (IllegalStateException e) {
            // Category is in use; show an error on the categories list page
            model.addAttribute("error", e.getMessage());
            model.addAttribute("categories", categoryService.listCategories());
            return "categories-list";
        }
    }

}
