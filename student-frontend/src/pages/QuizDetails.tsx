import { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import {
  getQuizById,
  getQuestionsByQuizId,
  type Quiz,
  type Question,
} from "../services/quizService";

function formatDate(dateStr: string): string {
  if (!dateStr) return "";
  const d = new Date(dateStr);
  if (Number.isNaN(d.getTime())) return dateStr;
  return d.toLocaleDateString("fi-FI");
}

export default function QuizDetails() {
  const { id } = useParams();
  const quizId = Number(id);

  const [quiz, setQuiz] = useState<Quiz | null>(null);
  const [questions, setQuestions] = useState<Question[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!quizId) {
      setError("Invalid quiz id");
      setLoading(false);
      return;
    }

    async function loadData() {
      try {
        setLoading(true);
        setError(null);

        const [quizData, questionData] = await Promise.all([
          getQuizById(quizId),
          getQuestionsByQuizId(quizId),
        ]);

        setQuiz(quizData);
        setQuestions(questionData);
      } catch (err: any) {
        setError(err.message || "Failed to load quiz details");
      } finally {
        setLoading(false);
      }
    }

    loadData();
  }, [quizId]);

  if (loading) {
    return (
      <div className="page-container">
        <p>Loading quiz...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="page-container">
        <p className="error-text">{error}</p>
        <Link to="/quizzes" className="back-link">
          ← Back to quizzes
        </Link>
      </div>
    );
  }

  if (!quiz) {
    return (
      <div className="page-container">
        <p>Quiz not found.</p>
        <Link to="/quizzes" className="back-link">
          ← Back to quizzes
        </Link>
      </div>
    );
  }

  const questionCount = questions.length;

  return (
    <div className="page-container">
      <Link to="/quizzes" className="back-link">
        ← Back to quizzes
      </Link>

      <h1 className="page-title">{quiz.name}</h1>
      <p className="quiz-description">{quiz.description}</p>

      <p className="quiz-meta">
        Added on: {formatDate(quiz.createdAt)} · Questions: {questionCount} ·
        Course: {quiz.courseCode} · Category: {quiz.categoryName}
      </p>

      <div className="questions-list">
        {questions.map((question, index) => (
          <div key={question.id} className="question-card">
            <p className="question-text">{question.text}</p>
            <p className="question-meta">
              Question {index + 1} of {questionCount} · Difficulty:{" "}
              {question.difficulty}
            </p>
          </div>
        ))}

        {questions.length === 0 && (
          <p>No questions found for this quiz.</p>
        )}
      </div>
    </div>
  );
}
