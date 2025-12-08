import { useParams, useNavigate } from "react-router-dom";
import ReviewForm from "../components/ReviewForm";

export default function AddReviewPage() {
  const { id } = useParams();
  const quizId = Number(id);
  const nav = useNavigate();

  return (
    <div className="page-container">
      <h1>Write a review</h1>

      <ReviewForm
        quizId={quizId}
        isPublished={true}
        onReviewAdded={() => nav(`/quizzes/${quizId}/reviews`)}
      />
    </div>
  );
}
