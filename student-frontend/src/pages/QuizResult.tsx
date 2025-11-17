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
      options: string[];
      correct: string[];
    }[];
  };
  answers: Record<number, string | string[]>;
};

export default function QuizResult() {
  const navigate = useNavigate();
  const { state } = useLocation() as { state: ResultState };
  const { result, quiz, answers } = state;

  console.log("QUIZ RESULT STATE:", state);

  return (
    <div style={{ padding: 20, maxWidth: 800, margin: "0 auto" }}>
      <button
        onClick={() => navigate("/")}
        style={{
          padding: "10px 18px",
          marginBottom: "20px",
          background: "#f3f4f6",
          border: "1px solid #ddd",
          borderRadius: "8px",
          cursor: "pointer",
        }}
      >
        ← Back to quizzes
      </button>

      <h1 style={{ fontSize: 36, fontWeight: 800 }}>Quiz Completed!</h1>
      <h2 style={{ fontSize: 28, color: "#2563eb", fontWeight: "600" }}>
        Score: {result.score} / {result.total}
      </h2>

      <h3 style={{ fontSize: 22, marginBottom: 25 }}>{quiz.title}</h3>

      {quiz.questions.map((q) => {
        const userAns = answers[q.id];
        const userArray = Array.isArray(userAns)
          ? userAns
          : userAns
          ? [userAns]
          : [];

        const correctArray = q.correct || [];

        const isCorrect =
          userArray.length === correctArray.length &&
          userArray.every((a) => correctArray.includes(a));

        return (
          <div
            key={q.id}
            style={{
              background: "#fff",
              padding: 20,
              marginBottom: 20,
              borderRadius: 12,
              border: "1px solid #ddd",
              boxShadow: "0 2px 8px rgba(0,0,0,0.05)",
            }}
          >
            <h3 style={{ fontSize: 18, fontWeight: "600" }}>{q.text}</h3>

            <p style={{ marginTop: 8 }}>
              <strong>Your answer:</strong>{" "}
              <span style={{ color: isCorrect ? "green" : "red" }}>
                {userArray.join(", ") || "No answer"}
              </span>
            </p>

            <p
              style={{
                marginTop: 5,
                fontWeight: "bold",
                color: isCorrect ? "green" : "red",
              }}
            >
              {isCorrect ? "Correct ✔" : "Wrong ✘"}
            </p>

            {!isCorrect && (
              <p style={{ marginTop: 8 }}>
                <strong>Correct answer:</strong>{" "}
                <span style={{ color: "#2563eb", fontWeight: 600 }}>
                  {correctArray.join(", ") || "Not provided"}
                </span>
              </p>
            )}
          </div>
        );
      })}
    </div>
  );
}
