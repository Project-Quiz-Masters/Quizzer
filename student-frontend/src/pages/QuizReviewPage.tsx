import { useParams, Link } from "react-router-dom";
import { useEffect, useState } from "react";
import { getQuizById, type Quiz } from "../services/quizService";
import { getReviewsByQuizId } from "../services/reviewService";
import EditReviewForm from "../components/EditReviewForm";

export interface Review {
  id: number;
  nickname: string;
  rating: number;
  text: string;
  createdAt: string;
}

export interface ReviewsResponse {
  reviews: Review[];
  averageRating: number;
  count: number;
}

export default function QuizReviewPage() {
  const { id } = useParams();
  const quizId = Number(id);

  const [quiz, setQuiz] = useState<Quiz | null>(null);
  const [reviewsData, setReviewsData] = useState<ReviewsResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const [editingReview, setEditingReview] = useState<Review | null>(null);

  useEffect(() => {
    if (!quizId) return;

    async function load() {
      try {
        const quizInfo = await getQuizById(quizId);
        const reviewInfo = await getReviewsByQuizId(quizId);

        setQuiz(quizInfo);
        setReviewsData(reviewInfo);
      } finally {
        setLoading(false);
      }
    }

    load();
  }, [quizId]);

  if (loading) return <p>Loading…</p>;
  if (!quiz) return <p>Quiz not found.</p>;

  const formattedAverage =
    reviewsData?.averageRating != null
      ? reviewsData.averageRating.toFixed(1)
      : "-";

  return (
    <div className="review-page-container">
      <h1>Reviews of "{quiz.title}"</h1>

      <p>
        {formattedAverage} rating average based on {reviewsData?.count ?? 0}{" "}
        reviews.
      </p>

      <Link to={`/quizzes/${quizId}/reviews/add`} className="quiz-link">
        Write your review
      </Link>
      {/* Edit form shown when user clicks 'Edit' on a review */}
      {editingReview && (
        <EditReviewForm
          review={editingReview}
          onCancel={() => setEditingReview(null)}
          onSaved={(updated) => {
            setReviewsData((prev) =>
              prev
                ? {
                    ...prev,
                    reviews: prev.reviews.map((r) =>
                      r.id === updated.id ? updated : r
                    ),
                  }
                : prev
            );
            setEditingReview(null);
          }}
        />
      )}

      <section className="reviews-list">
        {reviewsData?.reviews.length === 0 && <p>No reviews yet.</p>}

        {reviewsData?.reviews.map((review) => (
          <div className="review-card" key={review.id}>
            <strong>{review.nickname}</strong>
            <p>Rating: {review.rating}/5</p>
            <p>{review.text}</p>
            <div className="review-footer">
              <small className="review-date">
                Written on: {new Date(review.createdAt).toLocaleDateString()}
              </small>
              {/* Button to enable edit mode */}
              <button
                type="button"
                className="review-edit-button"
                onClick={() => setEditingReview(review)}
              >
                Edit
              </button>
            </div>
          </div>
        ))}
      </section>
    </div>
  );
}
