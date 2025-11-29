import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
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
  isCorrect: boolean | null;
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
  const [submitted, setSubmitted] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [feedback, setFeedback] = useState<string | null>(null);

  // Load quiz + questions
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

        // Initialize answers
        setUserAnswers(
          questionData.map((q) => ({
            questionId: q.id,
            selectedOptionId: null,
            isCorrect: null,
          }))
        );
      } catch (err: any) {
        setError(err.message || "Failed to load quiz details");
      } finally {
        setLoading(false);
      }
    }

    loadData();
  }, [quizId]);

  // Select answer
  const handleAnswerSelect = (
    questionId: number,
    optionId: number,
    isCorrect: boolean
  ) => {
    if (submitted) return;

    setUserAnswers((prev) =>
      prev.map((ua) =>
        ua.questionId === questionId
          ? { ...ua, selectedOptionId: optionId, isCorrect }
          : ua
      )
    );
  };

  // Submit quiz
  const handleSubmitQuiz = async () => {
    // Ensure all answered
    const unanswered = userAnswers.filter((a) => a.selectedOptionId === null);
    if (unanswered.length > 0) {
      alert("Please answer all questions before submitting.");
      return;
    }

    const totalQuestions = questions.length;
    const correctCount = userAnswers.filter((a) => a.isCorrect === true).length;
    const percentage = totalQuestions
      ? (correctCount / totalQuestions) * 100
      : 0;

    setSubmitting(true);

    const payload: SubmitQuizRequest = {
      quizId,
      answers: userAnswers.map((a) => ({
        questionId: a.questionId,
        answerOptionId: a.selectedOptionId,
      })),
    };

    try {
      await submitQuizAnswers(payload);
      setFeedback(
        `Quiz submitted! You scored ${correctCount} out of ${totalQuestions} (${percentage.toFixed(
          1
        )}%).`
      );
    } catch (err: any) {
      setFeedback(
        `Quiz submitted locally! You scored ${correctCount} out of ${totalQuestions} (${percentage.toFixed(
          1
        )}%). Saving to server failed: ${err.message || "Unknown error"}`
      );
    } finally {
      setSubmitted(true);
      setSubmitting(false);
    }
  };

  // Loading UI
  if (loading) {
    return (
      <div className="page-container">
        <p>Loading quiz...</p>
      </div>
    );
  }

  // Error UI
  if (error) {
    return (
      <div className="page-container">
        <p className="error-text">{error}</p>

        <button className="back-button" onClick={() => navigate(-1)}>
          ← Back to Quizzes
        </button>
      </div>
    );
  }

  if (!quiz) {
    return (
      <div className="page-container">
        <p>Quiz not found.</p>

        <button className="back-button" onClick={() => navigate(-1)}>
          ← Back to Quizzes
        </button>
      </div>
    );
  }

  const questionCount = questions.length;

  return (
    <div className="page-container">
      {/* Back button */}
      {!submitted && (
        <button className="back-button" onClick={() => navigate(-1)}>
          ← Back to Quizzes
        </button>
      )}

      <h1 className="page-title">{quiz.title}</h1>
      <p className="quiz-description">{quiz.description}</p>
      <p className="quiz-meta">
        Added on: {formatDate(quiz.createdAt)} · Questions: {questionCount} ·
        Course: {quiz.course} · Category: {(quiz as any).categoryName || "-"}
      </p>

      {feedback && (
        <div className={`feedback ${submitted ? "success" : "error"}`}>
          <p>{feedback}</p>
        </div>
      )}

      {/* Question list */}
      <div className="questions-list">
        {questions.map((question, index) => {
          const ua = userAnswers.find((a) => a.questionId === question.id);

          return (
            <div key={question.id} className="question-card">
              <p className="question-text">{question.text}</p>
              <p className="question-meta">
                Question {index + 1} of {questionCount} · Difficulty:{" "}
                {question.difficulty}
              </p>

              {submitted && (
                <p
                  className={`question-result ${ua?.isCorrect ? "correct" : "incorrect"
                    }`}
                >
                  {ua?.isCorrect ? "Correct" : "Wrong"}
                </p>
              )}

              {/* Answer options */}
              {question.answerOptions?.length > 0 && (
                <div className="answer-options">
                  {question.answerOptions.map((option) => {
                    const isSelected = ua?.selectedOptionId === option.id;

                    return (
                      <div
                        key={option.id}
                        className={`answer-option ${isSelected ? "selected" : ""
                          } ${submitted && isSelected && ua?.isCorrect
                            ? "correct"
                            : ""
                          } ${submitted && isSelected && !ua?.isCorrect
                            ? "incorrect"
                            : ""
                          } ${submitted && option.correct && !isSelected
                            ? "correct-answer"
                            : ""
                          }`}
                      >
                        <input
                          type="radio"
                          name={`q-${question.id}`}
                          checked={isSelected}
                          onChange={() =>
                            handleAnswerSelect(
                              question.id,
                              option.id,
                              option.correct
                            )
                          }
                          disabled={submitted}
                        />
                        <label>{option.text}</label>
                      </div>
                    );
                  })}
                </div>
              )}
            </div>
          );
        })}
      </div>

      {/* Submit */}
      {!submitted && questions.length > 0 && (
        <div className="submit-section">
          <button
            className="submit-button"
            onClick={handleSubmitQuiz}
            disabled={submitting}
          >
            {submitting ? "Submitting..." : "Submit Quiz"}
          </button>
        </div>
      )}

      {/* Back after submit */}
      {submitted && (
        <div className="submit-section">
          <button className="back-button" onClick={() => navigate(-1)}>
            Back to Quizzes
          </button>
        </div>
      )}
    </div>
  );
}
