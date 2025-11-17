import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

type Quiz = {
  id: number;
  title: string;
  description: string;
};

export default function QuizList() {
  const [quizzes, setQuizzes] = useState<Quiz[]>([]);
  const navigate = useNavigate();

  useEffect(() => {
    fetch("http://localhost:8080/api/quizzes/published")
      .then((res) => res.json())
      .then((data) => setQuizzes(data))
      .catch((err) => console.error("Error fetching quizzes:", err));
  }, []);

  return (
    <div
      style={{
        padding: "40px 20px",
        maxWidth: "800px",
        margin: "0 auto",
        fontFamily: "Arial, sans-serif",
      }}
    >
      <h1
        style={{
          fontSize: "36px",
          fontWeight: "700",
          marginBottom: "25px",
          textAlign: "center",
        }}
      >
        Available Quizzes
      </h1>

      {quizzes.length === 0 && (
        <p
          style={{
            textAlign: "center",
            fontSize: "18px",
            color: "#666",
            marginTop: "40px",
          }}
        >
          No published quizzes yet.
        </p>
      )}

      <div style={{ display: "flex", flexDirection: "column", gap: "20px" }}>
        {quizzes.map((quiz) => (
          <div
            key={quiz.id}
            onClick={() => navigate(`/quiz/${quiz.id}`)}
            style={{
              padding: "20px",
              borderRadius: "14px",
              border: "1px solid #e5e7eb",
              background: "white",
              boxShadow: "0 2px 8px rgba(0,0,0,0.05)",
              cursor: "pointer",
              transition: "0.2s",
            }}
            onMouseEnter={(e) =>
              (e.currentTarget.style.boxShadow = "0 4px 12px rgba(0,0,0,0.12)")
            }
            onMouseLeave={(e) =>
              (e.currentTarget.style.boxShadow = "0 2px 8px rgba(0,0,0,0.05)")
            }
          >
            <h3
              style={{
                fontSize: "20px",
                fontWeight: "600",
                marginBottom: "8px",
              }}
            >
              {quiz.title}
            </h3>
            <p style={{ color: "#555", fontSize: "15px", lineHeight: "1.4" }}>
              {quiz.description}
            </p>

            <button
              style={{
                marginTop: "12px",
                padding: "8px 14px",
                background: "#2563eb",
                color: "white",
                border: "none",
                borderRadius: "6px",
                cursor: "pointer",
                fontSize: "14px",
              }}
            >
              Start Quiz →
            </button>
          </div>
        ))}
      </div>
    </div>
  );
}
