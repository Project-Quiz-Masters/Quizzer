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
    fetch("http://localhost:8080/api/quizzes")
      .then((res) => res.json())
      .then((data) => setQuizzes(data))
      .catch((err) => console.error("Error fetching quizzes:", err));
  }, []);

  return (
    <div style={{ padding: 20 }}>
      <h1>Available Quizzes</h1>

      {quizzes.map((quiz) => (
        <div
          key={quiz.id}
          onClick={() => navigate(`/quiz/${quiz.id}`)}
          style={{
            padding: 15,
            border: "1px solid #ccc",
            borderRadius: 8,
            marginBottom: 10,
            cursor: "pointer",
          }}
        >
          <h3>{quiz.title}</h3>
          <p>{quiz.description}</p>
        </div>
      ))}
    </div>
  );
}
