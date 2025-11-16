import { useLocation, useNavigate } from "react-router-dom";

type ResultState = {
  result: {
    score: number;
    total: number;
  };
  quiz: {
    title: string;
    questions: {
      id: number;
      text: string;
      type: string;
      options: string[];
      correct?: string[];
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

      <h1>Quiz Completed!</h1>

      <h2>
        Score: {result.score} / {result.total}
      </h2>

      <h3>{quiz.title}</h3>

      {quiz.questions.map((q) => {
        const userAnswer = answers[q.id];

        return (
          <div key={q.id} style={{ marginBottom: 20 }}>
            <h3>{q.text}</h3>

            <p>
              <strong>Your answer:</strong>{" "}
              {Array.isArray(userAnswer)
                ? userAnswer.join(", ")
                : userAnswer ?? "No answer"}
            </p>
          </div>
        );
      })}
    </div>
  );
}
