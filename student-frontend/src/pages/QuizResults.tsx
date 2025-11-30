import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getQuizResults } from "../services/quizResults";
import type { QuestionResult } from "../services/quizResults";

export default function QuizResults() {
  const { id } = useParams<{ id: string }>();
  const [results, setResults] = useState<QuestionResult[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!id) return;

    getQuizResults(Number(id))
      .then((data) => setResults(data))
      .finally(() => setLoading(false));
  }, [id]);

  if (loading) return <p>Loading...</p>;
  if (!results.length) return <p>No answers yet for this quiz.</p>;

  const totalQuestions = results.length;
  const totalAnswers = results.reduce((sum, r) => sum + r.totalAnswers, 0);

  return (
    <div className="results-container">
      <h1 className="results-title">Quiz Results</h1>

      <p className="results-summary">
        {totalAnswers} answers to {totalQuestions} questions
      </p>

      <div className="results-table-wrapper">
        <table className="results-table">
          <thead>
            <tr>
              <th>Question</th>
              <th>Difficulty</th>
              <th>Total answers</th>
              <th>Correct answer %</th>
              <th>Correct answers</th>
              <th>Wrong answers</th>
            </tr>
          </thead>
          <tbody>
            {results.map((r) => {
              const correctPercent =
                r.totalAnswers === 0
                  ? "N/A"
                  : `${Math.round((r.correctAnswers / r.totalAnswers) * 100)}%`;

              return (
                <tr key={r.questionId}>
                  <td>{r.questionText}</td>
                  <td>{r.questionDifficulty}</td>
                  <td>{r.totalAnswers}</td>
                  <td>{correctPercent}</td>
                  <td>{r.correctAnswers}</td>
                  <td>{r.wrongAnswers}</td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>
    </div>
  );
}
