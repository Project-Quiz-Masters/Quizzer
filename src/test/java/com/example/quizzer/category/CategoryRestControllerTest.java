package com.example.quizzer.category;

import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CategoryRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        categoryRepository.deleteAll();
    }

    // ============ GET /api/categories ============

    @Test
    void getAllCategoriesReturnsEmptyListWhenNoCategoriesExist() throws Exception {
        // Act + Assert
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getAllCategoriesReturnsListOfCategoriesWhenCategoriesExist() throws Exception {
        // Arrange
        Category cat1 = new Category("Mathematics", "Math courses");
        Category cat2 = new Category("Science", "Science courses");
        categoryRepository.saveAll(List.of(cat1, cat2));

        // Act + Assert
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("Mathematics", "Science")));
    }

    // ============ GET /api/categories/{categoryId} ============

    @Test
    void getCategoryByIdReturnsNotFoundWhenCategoryDoesNotExist() throws Exception {
        // Act + Assert
        mockMvc.perform(get("/api/categories/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCategoryByIdReturnsOkAndCategoryWhenCategoryExists() throws Exception {
        // Arrange
        Category category = new Category("History", "History courses");
        Category saved = categoryRepository.save(category);

        // Act + Assert
        mockMvc.perform(get("/api/categories/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.name").value("History"))
                .andExpect(jsonPath("$.description").value("History courses"));
    }

    // ============ POST /api/categories ============

    @Test
    void createCategoryReturnsCreatedAndNewCategory() throws Exception {
        // Arrange
        Category payload = new Category("Physics", "Physics courses");
        String jsonPayload = mapper.writeValueAsString(payload);

        // Act + Assert
        mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Physics"))
                .andExpect(jsonPath("$.description").value("Physics courses"))
                .andExpect(jsonPath("$.id").exists());

        // Verify saved in database
        List<Category> all = categoryRepository.findAll();
        assert all.size() == 1;
        assert all.get(0).getName().equals("Physics");
    }

    @Test
    void createCategoryReturnsBadRequestWhenNameIsNull() throws Exception {
        // Arrange
        Category payload = new Category(null, "Some description");
        String jsonPayload = mapper.writeValueAsString(payload);

        // Act + Assert
        mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isBadRequest());

        // Verify not saved
        List<Category> all = categoryRepository.findAll();
        assert all.isEmpty();
    }

    @Test
    void createCategoryReturnsBadRequestWhenNameIsBlank() throws Exception {
        // Arrange
        Category payload = new Category("   ", "Some description");
        String jsonPayload = mapper.writeValueAsString(payload);

        // Act + Assert
        mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isBadRequest());

        // Verify not saved
        List<Category> all = categoryRepository.findAll();
        assert all.isEmpty();
    }

    @Test
    void createCategoryReturnsBadRequestWhenPayloadIsNull() throws Exception {
        // Act + Assert
        mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content("null"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createCategoryWithOptionalDescriptionIsNull() throws Exception {
        // Arrange
        Category payload = new Category("Economics", null);
        String jsonPayload = mapper.writeValueAsString(payload);

        // Act + Assert
        mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Economics"))
                .andExpect(jsonPath("$.description").doesNotExist());
    }

    // ============ PUT /api/categories/{categoryId} ============

    @Test
    void updateCategoryReturnsOkAndUpdatedCategory() throws Exception {
        // Arrange
        Category existing = new Category("Old Name", "Old Description");
        Category saved = categoryRepository.save(existing);

        Category updatePayload = new Category("New Name", "New Description");
        String jsonPayload = mapper.writeValueAsString(updatePayload);

        // Act + Assert
        mockMvc.perform(put("/api/categories/" + saved.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.name").value("New Name"))
                .andExpect(jsonPath("$.description").value("New Description"));

        // Verify database update
        Category updated = categoryRepository.findById(saved.getId()).orElse(null);
        assert updated != null;
        assert updated.getName().equals("New Name");
        assert updated.getDescription().equals("New Description");
    }

    @Test
    void updateCategoryReturnsNotFoundWhenCategoryDoesNotExist() throws Exception {
        // Arrange
        Category updatePayload = new Category("Some Name", "Some Description");
        String jsonPayload = mapper.writeValueAsString(updatePayload);

        // Act + Assert
        mockMvc.perform(put("/api/categories/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateCategoryPartialUpdateNameOnly() throws Exception {
        // Arrange
        Category existing = new Category("Original Name", "Original Description");
        Category saved = categoryRepository.save(existing);

        // Only update name (description might remain or be updated based on logic)
        Category updatePayload = new Category("Updated Name", "Original Description");
        String jsonPayload = mapper.writeValueAsString(updatePayload);

        // Act + Assert
        mockMvc.perform(put("/api/categories/" + saved.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    // ============ DELETE /api/categories/{categoryId} ============

    @Test
    void deleteCategoryReturnsNoContentWhenCategoryExists() throws Exception {
        // Arrange
        Category category = new Category("To Delete", "Description");
        Category saved = categoryRepository.save(category);

        // Act + Assert
        mockMvc.perform(delete("/api/categories/" + saved.getId()))
                .andExpect(status().isNoContent());

        // Verify deleted from database
        boolean exists = categoryRepository.existsById(saved.getId());
        assert !exists;
    }

    @Test
    void deleteCategoryReturnsNotFoundWhenCategoryDoesNotExist() throws Exception {
        // Act + Assert
        mockMvc.perform(delete("/api/categories/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCategoryAndVerifyNotAccessibleAfterDeletion() throws Exception {
        // Arrange
        Category category = new Category("Temporary", "Will be deleted");
        Category saved = categoryRepository.save(category);

        // Delete
        mockMvc.perform(delete("/api/categories/" + saved.getId()))
                .andExpect(status().isNoContent());

        // Verify cannot GET deleted category
        mockMvc.perform(get("/api/categories/" + saved.getId()))
                .andExpect(status().isNotFound());
    }
}
