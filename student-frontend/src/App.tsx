import { useEffect, useState } from "react";
import "./App.css";

type Quiz = {
  id: number;                 
  title: string;
  description?: string;
  course?: string;
  published: boolean;
  createdAt?: string;       
  teacherId?: number;
};

export default function App() {
  const [quizzes, setQuizzes] = useState<Quiz[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    fetch("http://localhost:8080/api/quizzes")
      .then((res) => {
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        return res.json() as Promise<Quiz[]>;
      })
      .then((data) => setQuizzes(data))
      .catch((e) => setError(e.message))
      .finally(() => setLoading(false));
  }, []);

  return (
    <div className="App">
      <header className="App-header" style={{ padding: 16 }}>
        <h1>Welcome to the Student Frontend</h1>
      </header>

      <main style={{ padding: 24, fontFamily: "system-ui, sans-serif" }}>
        <h2>Published Quizzes</h2>

        {loading && <p>Loading published quizzes…</p>}
        {error && <p style={{ color: "red" }}>Error: {error}</p>}

        {!loading && !error && (
          quizzes.length === 0 ? (
            <p>No published quizzes available.</p>
          ) : (
            <ul>
              {quizzes.map((q) => (
                <li key={q.id}>
                  <strong>{q.title || "Untitled Quiz"}</strong>
                  {q.description ? <> — {q.description}</> : null}
                </li>
              ))}
            </ul>
          )
        )}
      </main>
    </div>
  );
}
