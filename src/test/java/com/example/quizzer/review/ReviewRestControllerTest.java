package com.example.quizzer.review;

import java.util.List;

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

import com.example.quizzer.quiz.Quiz;
import com.example.quizzer.quiz.QuizRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ReviewRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private QuizRepository quizRepository;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        reviewRepository.deleteAll();
        quizRepository.deleteAll();
    }

    private Quiz createAndSaveQuiz(String title) {
        Quiz quiz = new Quiz(title, "Description", "Course", 1L);
        return quizRepository.save(quiz);
    }

    // ============ POST /api/quizzes/{quizId}/reviews ============

    @Test
    void addReviewReturnsCreatedWhenReviewDataIsValid() throws Exception {
        // Arrange
        Quiz quiz = createAndSaveQuiz("Test Quiz");
        Review payload = new Review("John", 5, "Excellent quiz!", quiz);
        String jsonPayload = mapper.writeValueAsString(payload);

        // Act + Assert
        mockMvc.perform(post("/api/quizzes/" + quiz.getId() + "/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nickname").value("John"))
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$.text").value("Excellent quiz!"))
                .andExpect(jsonPath("$.id").exists());

        // Verify saved in database
        List<Review> allReviews = reviewRepository.findAll();
        assert allReviews.size() == 1;
        assert allReviews.get(0).getNickname().equals("John");
    }

    @Test
    void addReviewWithDifferentRatings() throws Exception {
        // Arrange
        Quiz quiz = createAndSaveQuiz("Test Quiz");

        // Test ratings from 1 to 5
        for (int rating = 1; rating <= 5; rating++) {
            Review payload = new Review("User" + rating, rating, "Review with rating " + rating, quiz);
            String jsonPayload = mapper.writeValueAsString(payload);

            mockMvc.perform(post("/api/quizzes/" + quiz.getId() + "/reviews")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonPayload))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.rating").value(rating));
        }

        // Verify all reviews saved
        List<Review> allReviews = reviewRepository.findAll();
        assert allReviews.size() == 5;
    }

    @Test
    void addReviewWithValidDataPersistsSuccessfully() throws Exception {
        // Arrange
        Quiz quiz = createAndSaveQuiz("Test Quiz");
        Review payload = new Review("ValidUser", 4, "Good quiz", quiz);
        String jsonPayload = mapper.writeValueAsString(payload);

        // Act + Assert
        mockMvc.perform(post("/api/quizzes/" + quiz.getId() + "/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nickname").value("ValidUser"));

        // Verify saved
        List<Review> allReviews = reviewRepository.findAll();
        assert allReviews.size() == 1;
    }

    @Test
    void addReviewWithEmptyTextStillCreates() throws Exception {
        // Arrange
        Quiz quiz = createAndSaveQuiz("Test Quiz");
        Review payload = new Review("Alice", 3, "", quiz);
        String jsonPayload = mapper.writeValueAsString(payload);

        // Act + Assert (empty text might be allowed)
        mockMvc.perform(post("/api/quizzes/" + quiz.getId() + "/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isCreated());
    }

    // ============ GET /api/quizzes/{quizId}/reviews ============

    @Test
    void getReviewsReturnsEmptyListWhenQuizHasNoReviews() throws Exception {
        // Arrange
        Quiz quiz = createAndSaveQuiz("Test Quiz");

        // Act + Assert
        mockMvc.perform(get("/api/quizzes/" + quiz.getId() + "/reviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviews", hasSize(0)))
                .andExpect(jsonPath("$.averageRating").value(0.0))
                .andExpect(jsonPath("$.count").value(0));
    }

    @Test
    void getReviewsReturnsListAndStatsWhenQuizHasReviews() throws Exception {
        // Arrange
        Quiz quiz = createAndSaveQuiz("Test Quiz");
        Review rev1 = new Review("Alice", 5, "Great!", quiz);
        Review rev2 = new Review("Bob", 3, "Okay", quiz);
        Review rev3 = new Review("Charlie", 4, "Good", quiz);
        reviewRepository.saveAll(List.of(rev1, rev2, rev3));

        // Act + Assert
        mockMvc.perform(get("/api/quizzes/" + quiz.getId() + "/reviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviews", hasSize(3)))
                .andExpect(jsonPath("$.count").value(3))
                .andExpect(jsonPath("$.averageRating").value(4.0)); // (5+3+4)/3 = 4
    }

    @Test
    void getReviewsReturnsNewestFirst() throws Exception {
        // Arrange
        Quiz quiz = createAndSaveQuiz("Test Quiz");
        Review rev1 = new Review("First", 1, "First review", quiz);
        Review rev2 = new Review("Second", 2, "Second review", quiz);
        Review rev3 = new Review("Third", 3, "Third review", quiz);
        reviewRepository.saveAll(List.of(rev1, rev2, rev3));

        // Act + Assert - verify order (newest first)
        // This assumes reviews are returned in reverse creation order
        mockMvc.perform(get("/api/quizzes/" + quiz.getId() + "/reviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviews", hasSize(3)))
                .andExpect(jsonPath("$.reviews[0].nickname").value("Third")); // Newest
    }

    @Test
    void getReviewsWithSingleReviewCalculatesAverageCorrectly() throws Exception {
        // Arrange
        Quiz quiz = createAndSaveQuiz("Test Quiz");
        Review review = new Review("Solo", 3, "Single review", quiz);
        reviewRepository.save(review);

        // Act + Assert
        mockMvc.perform(get("/api/quizzes/" + quiz.getId() + "/reviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviews", hasSize(1)))
                .andExpect(jsonPath("$.averageRating").value(3.0))
                .andExpect(jsonPath("$.count").value(1));
    }

    // ============ PUT /api/reviews/{reviewId} ============

    @Test
    void updateReviewReturnsOkAndUpdatedReview() throws Exception {
        // Arrange
        Quiz quiz = createAndSaveQuiz("Test Quiz");
        Review existing = new Review("Original", 2, "Original text", quiz);
        Review saved = reviewRepository.save(existing);

        Review updatePayload = new Review("Updated", 5, "Updated text", quiz);
        String jsonPayload = mapper.writeValueAsString(updatePayload);

        // Act + Assert
        mockMvc.perform(put("/api/reviews/" + saved.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value("Updated"))
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$.text").value("Updated text"));

        // Verify database update
        Review updated = reviewRepository.findById(saved.getId()).orElse(null);
        assert updated != null;
        assert updated.getNickname().equals("Updated");
        assert updated.getRating() == 5;
    }

    @Test
    void updateReviewReturnsNotFoundWhenReviewDoesNotExist() throws Exception {
        // Arrange
        Quiz quiz = createAndSaveQuiz("Test Quiz");
        Review payload = new Review("Some", 3, "Some text", quiz);
        String jsonPayload = mapper.writeValueAsString(payload);

        // Act + Assert
        mockMvc.perform(put("/api/reviews/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateReviewWithHighRatingIsAccepted() throws Exception {
        // Arrange
        Quiz quiz = createAndSaveQuiz("Test Quiz");
        Review existing = new Review("Test", 3, "Text", quiz);
        Review saved = reviewRepository.save(existing);

        // Update with a higher rating
        Review updatePayload = new Review("Updated", 5, "Updated rating", quiz);
        String jsonPayload = mapper.writeValueAsString(updatePayload);

        // Act + Assert
        mockMvc.perform(put("/api/reviews/" + saved.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(5));
    }

    // ============ DELETE /api/reviews/{reviewId} ============

    @Test
    void deleteReviewReturnsNoContentWhenReviewExists() throws Exception {
        // Arrange
        Quiz quiz = createAndSaveQuiz("Test Quiz");
        Review review = new Review("ToDelete", 2, "Will be deleted", quiz);
        Review saved = reviewRepository.save(review);

        // Act + Assert
        mockMvc.perform(delete("/api/reviews/" + saved.getId()))
                .andExpect(status().isNoContent());

        // Verify deleted from database
        boolean exists = reviewRepository.existsById(saved.getId());
        assert !exists;
    }

    @Test
    void deleteReviewReturnsNotFoundWhenReviewDoesNotExist() throws Exception {
        // Act + Assert
        mockMvc.perform(delete("/api/reviews/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteReviewAndVerifyCannotAccessAfterDeletion() throws Exception {
        // Arrange
        Quiz quiz = createAndSaveQuiz("Test Quiz");
        Review review = new Review("Temp", 3, "Temporary", quiz);
        Review saved = reviewRepository.save(review);

        // Delete
        mockMvc.perform(delete("/api/reviews/" + saved.getId()))
                .andExpect(status().isNoContent());

        // Try to update deleted review (should fail)
        Review updatePayload = new Review("Updated", 4, "Should fail", quiz);
        String jsonPayload = mapper.writeValueAsString(updatePayload);

        mockMvc.perform(put("/api/reviews/" + saved.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteReviewUpdatesQuizAverageRating() throws Exception {
        // Arrange
        Quiz quiz = createAndSaveQuiz("Test Quiz");
        Review rev1 = new Review("Alice", 5, "Great", quiz);
        Review rev2 = new Review("Bob", 3, "Okay", quiz);
        Review saved1 = reviewRepository.save(rev1);
        reviewRepository.save(rev2);

        // Initial average should be 4.0
        mockMvc.perform(get("/api/quizzes/" + quiz.getId() + "/reviews"))
                .andExpect(jsonPath("$.averageRating").value(4.0))
                .andExpect(jsonPath("$.count").value(2));

        // Delete one review
        mockMvc.perform(delete("/api/reviews/" + saved1.getId()))
                .andExpect(status().isNoContent());

        // Average should now be 3.0 (only Bob's review remains)
        mockMvc.perform(get("/api/quizzes/" + quiz.getId() + "/reviews"))
                .andExpect(jsonPath("$.averageRating").value(3.0))
                .andExpect(jsonPath("$.count").value(1));
    }

    @Test
    void multipleReviewsForSameQuizAreIndependent() throws Exception {
        // Arrange
        Quiz quiz = createAndSaveQuiz("Test Quiz");
        Review rev1 = new Review("User1", 5, "Excellent", quiz);
        Review rev2 = new Review("User2", 2, "Poor", quiz);
        Review saved1 = reviewRepository.save(rev1);
        reviewRepository.save(rev2);

        // Delete one review
        mockMvc.perform(delete("/api/reviews/" + saved1.getId()))
                .andExpect(status().isNoContent());

        // Verify other review still exists
        mockMvc.perform(get("/api/quizzes/" + quiz.getId() + "/reviews"))
                .andExpect(jsonPath("$.reviews", hasSize(1)))
                .andExpect(jsonPath("$.reviews[0].nickname").value("User2"));
    }
}
