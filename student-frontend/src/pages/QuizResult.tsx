import { useLocation, useNavigate } from "react-router-dom";

type ResultState = {
  result: {
    correctAnswers: Record<number, string | string[]>;
  };
  quiz: {
    title: string;
    questions: {
      id: number;
      text: string;
      type: string;
      options: string[];
    }[];
  };
  answers: Record<number, string | string[]>;
};
export default function QuizResult() {
  const navigate = useNavigate();
  const { state } = useLocation() as { state: ResultState };
  const { result, quiz, answers } = state;

  return (
    <div style={{ padding: 20 }}>
      <button onClick={() => navigate("/")}>← Back to quizzes</button>

      <h1>Quiz Results</h1>

      {quiz.questions.map((q) => {
        const userAnswer = answers[q.id];
        const correct = result.correctAnswers[q.id];

        const match = JSON.stringify(userAnswer) === JSON.stringify(correct);

        return (
          <div key={q.id} style={{ marginBottom: 20 }}>
            <h3>
              {q.text}{" "}
              {match ? (
                <span style={{ color: "green" }}>✔ Correct</span>
              ) : (
                <span style={{ color: "red" }}>✘ Wrong</span>
              )}
            </h3>

            {!match && (
              <p style={{ color: "green" }}>
                Correct answer: {JSON.stringify(correct)}
              </p>
            )}
          </div>
        );
      })}
    </div>
  );
}
