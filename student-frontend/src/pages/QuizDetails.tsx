import { useEffect, useState } from "react";
import { useParams, useNavigate, Link } from "react-router-dom";
import {
  getQuizById,
  getQuestionsByQuizId,
  submitQuizAnswers,
  type Quiz,
  type Question,
  type SubmitQuizRequest,
} from "../services/quizService";

function formatDate(dateStr: string): string {
  if (!dateStr) return "";
  const d = new Date(dateStr);
  if (Number.isNaN(d.getTime())) return dateStr;
  return d.toLocaleDateString("fi-FI");
}

interface UserAnswer {
  questionId: number;
  selectedOptionId: number | null;
}

export default function QuizDetails() {
  const { id } = useParams();
  const quizId = Number(id);
  const navigate = useNavigate();

  const [quiz, setQuiz] = useState<Quiz | null>(null);
  const [questions, setQuestions] = useState<Question[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const [userAnswers, setUserAnswers] = useState<UserAnswer[]>([]);
  const [feedback, setFeedback] = useState<Record<number, string>>({});

  useEffect(() => {
    async function load() {
      try {
        const [quizData, questionData] = await Promise.all([
          getQuizById(quizId),
          getQuestionsByQuizId(quizId),
        ]);

        setQuiz(quizData);
        setQuestions(questionData);

        setUserAnswers(
          questionData.map((q) => ({
            questionId: q.id,
            selectedOptionId: null,
          }))
        );
      } catch (err: any) {
        setError(err.message || "Failed to load quiz");
      } finally {
        setLoading(false);
      }
    }
    load();
  }, [quizId]);

  const handleSelect = (questionId: number, optionId: number) => {
    setUserAnswers((prev) =>
      prev.map((a) =>
        a.questionId === questionId ? { ...a, selectedOptionId: optionId } : a
      )
    );
  };

  const handleSubmitAnswer = (question: Question) => {
    const ua = userAnswers.find((a) => a.questionId === question.id);
    if (!ua || ua.selectedOptionId == null) return;

    const selectedOpt = question.answerOptions.find(
      (o) => o.id === ua.selectedOptionId
    );
    if (!selectedOpt) return;

    setFeedback((prev) => ({
      ...prev,
      [question.id]: selectedOpt.correct
        ? "That is correct, good job!"
        : "That is not correct, try again!",
    }));
  };

  const handleSubmitQuiz = async () => {
    const payload: SubmitQuizRequest = {
      quizId,
      answers: userAnswers.map((a) => ({
        questionId: a.questionId,
        answerOptionId: a.selectedOptionId,
      })),
    };

    try {
      await submitQuizAnswers(payload);
      navigate("/quizzes");
    } catch (err: any) {
      alert("Failed to submit quiz.");
    }
  };

  if (loading) return <p>Loading quiz...</p>;
  if (error) return <p>{error}</p>;
  if (!quiz) return <p>Quiz not found.</p>;

  return (
    <div className="page-container">
      <button className="back-button" onClick={() => navigate(-1)}>
        ← Back to Quizzes
      </button>

      <h1 className="page-title">{quiz.title}</h1>
      <p className="quiz-description">{quiz.description}</p>
      <p className="quiz-meta">
        Added on: {formatDate(quiz.createdAt)} · Questions: {questions.length} ·
        Course: {quiz.course} · Category: {(quiz as any).categoryName || "-"}
      </p>

      {questions.map((question, idx) => {
        const ua = userAnswers.find((a) => a.questionId === question.id);

        return (
          <div key={question.id} className="question-card">
            <p className="question-text">{question.text}</p>
            <p className="question-meta">
              Question {idx + 1} of {questions.length} · Difficulty: {question.difficulty}
            </p>

            <div className="answer-options">
              {question.answerOptions.map((opt) => (
                <label key={opt.id} className="answer-option">
                  <input
                    type="radio"
                    name={`q-${question.id}`}
                    checked={ua?.selectedOptionId === opt.id}
                    onChange={() => handleSelect(question.id, opt.id)}
                  />
                  {opt.text}
                </label>
              ))}
            </div>

            <button
              className="submit-button"
              onClick={() => handleSubmitAnswer(question)}
            >
              Submit Answer
            </button>

            {feedback[question.id] && (
              <p
                style={{
                  marginTop: "8px",
                  color: feedback[question.id].includes("correct") ? "green" : "red",
                }}
              >
                {feedback[question.id]}
              </p>
            )}
          </div>
        );
      })}

      <div style={{ display: "flex", gap: "1rem", marginTop: "1rem" }}>
        <button className="submit-button" onClick={handleSubmitQuiz}>
          Submit Quiz
        </button>
        <Link
          to={`/quizzes/${quiz.id}/review`}
          className="submit-button"
          style={{ textDecoration: "none", textAlign: "center" }}
        >
          Add Review
        </Link>
      </div>
    </div>
  );
}
