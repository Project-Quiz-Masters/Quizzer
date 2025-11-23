import { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import {
  getQuizById,
  getQuestionsByQuizId,
  submitQuizAnswers,
  type Quiz,
  type Question,
  type AnswerOption,
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

  const [quiz, setQuiz] = useState<Quiz | null>(null);
  const [questions, setQuestions] = useState<Question[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [userAnswers, setUserAnswers] = useState<UserAnswer[]>([]);
  const [submitted, setSubmitted] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [feedback, setFeedback] = useState<string | null>(null);

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

  const handleAnswerSelect = (
    questionId: number,
    optionId: number,
    isCorrect: boolean
  ) => {
    setUserAnswers((prev) =>
      prev.map((answer) =>
        answer.questionId === questionId
          ? { ...answer, selectedOptionId: optionId, isCorrect }
          : answer
      )
    );
  };

  const handleSubmitQuiz = async () => {
    const totalQuestions = questions.length;
    const correctCount = userAnswers.filter((a) => a.isCorrect === true).length;
    const percentage = totalQuestions
      ? (correctCount / totalQuestions) * 100
      : 0;

    setSubmitting(true);
    setFeedback(null);

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
        )}%). Your answers were saved.`
      );
    } catch (err: any) {
      console.error("Submitting quiz answers failed:", err);
      setFeedback(
        `Quiz submitted locally! You scored ${correctCount} out of ${totalQuestions} (${percentage.toFixed(
          1
        )}%). However, saving results to the server failed: ${
          err.message || "Unknown error"
        }`
      );
    } finally {
      setSubmitted(true);
      setSubmitting(false);
    }
  };

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
      {/* Task 113: Back button */}
      <Link to="/quizzes" className="back-link">
        ← Back to quizzes
      </Link>

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

      <div className="questions-list">
        {questions.map((question, index) => {
          const userAnswer = userAnswers.find(
            (a) => a.questionId === question.id
          );
          const isAnsweredCorrectly = userAnswer?.isCorrect === true;

          return (
            <div key={question.id} className="question-card">
              <p className="question-text">{question.text}</p>
              <p className="question-meta">
                Question {index + 1} of {questionCount} · Difficulty:{" "}
                {question.difficulty}
              </p>

              {/* Task 117: per-question Correct/Wrong marker */}
              {submitted && (
                <p
                  className={`question-result ${
                    isAnsweredCorrectly ? "correct" : "incorrect"
                  }`}
                >
                  {isAnsweredCorrectly ? "Correct" : "Wrong"}
                </p>
              )}

              {question.answerOptions && question.answerOptions.length > 0 && (
                <div className="answer-options">
                  {question.answerOptions.map((option: AnswerOption) => {
                    const isSelected =
                      userAnswer?.selectedOptionId === option.id;
                    const answerSubmitted =
                      submitted && userAnswer?.selectedOptionId === option.id;
                    const isCorrectAnswer =
                      submitted && option.isCorrect && !isSelected;

                    return (
                      <div
                        key={option.id}
                        className={`answer-option ${
                          isSelected ? "selected" : ""
                        } ${
                          answerSubmitted && userAnswer?.isCorrect
                            ? "correct"
                            : ""
                        } ${
                          answerSubmitted && !userAnswer?.isCorrect
                            ? "incorrect"
                            : ""
                        } ${isCorrectAnswer ? "correct-answer" : ""}`}
                        onClick={() => {
                          if (!submitted) {
                            handleAnswerSelect(
                              question.id,
                              option.id,
                              option.isCorrect
                            );
                          }
                        }}
                      >
                        <input
                          type="radio"
                          name={`question-${question.id}`}
                          checked={isSelected}
                          onChange={() => {
                            if (!submitted) {
                              handleAnswerSelect(
                                question.id,
                                option.id,
                                option.isCorrect
                              );
                            }
                          }}
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

        {questions.length === 0 && <p>No questions found for this quiz.</p>}
      </div>

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

      {submitted && (
        <div className="submit-section">
          <Link to="/quizzes" className="back-button">
            Back to Quizzes
          </Link>
        </div>
      )}
    </div>
  );
}
