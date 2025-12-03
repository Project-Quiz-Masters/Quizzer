import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import type { Quiz } from "../services/quizService"; 
import { getQuizById } from "../services/quizService";
import ReviewForm from "../components/ReviewForm";

export default function QuizReviewPage() {
  const { id } = useParams();
  const quizId = Number(id);

  const [quiz, setQuiz] = useState<Quiz | null>(null);

  useEffect(() => {
    if (!quizId) return;
    getQuizById(quizId).then(setQuiz).catch(() => setQuiz(null));
  }, [quizId]);

  if (!quiz) return <p>Loading quiz...</p>;

  return (
    <div className="page-container">
      <h1>Add a review for "{quiz.title}"</h1>
      <ReviewForm quizId={quiz.id} isPublished={quiz.published} />
    </div>
  );
}
