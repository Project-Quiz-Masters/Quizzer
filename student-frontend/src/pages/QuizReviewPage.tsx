import { useParams, Link } from "react-router-dom";
import { useEffect, useState } from "react";
import { getQuizById, type Quiz } from "../services/quizService";
import { getReviewsByQuizId } from "../services/reviewService";

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

  return (
    <div className="review-page-container">
      <h1>Reviews of "{quiz.title}"</h1>

      <p>
        {reviewsData?.averageRating ?? "-"} rating average based on{" "}
        {reviewsData?.count ?? 0} reviews.
      </p>

      <Link to={`/quizzes/${quizId}/reviews/add`} className="quiz-link">
        Write your review
      </Link>

      <section className="reviews-list">
        {reviewsData?.reviews.length === 0 && <p>No reviews yet.</p>}

        {reviewsData?.reviews.map((review) => (
          <div className="review-card" key={review.id}>
            <strong>{review.nickname}</strong>
            <p>Rating: {review.rating}/5</p>
            <p>{review.text}</p>
            <small>
              Written on: {new Date(review.createdAt).toLocaleDateString()}
            </small>
          </div>
        ))}
      </section>
    </div>
  );
}
