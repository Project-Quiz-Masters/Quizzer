import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";

type Question = {
  id: number;
  text: string;
  type: "single" | "multiple";
  options: string[];
};

type Quiz = {
  id: number;
  title: string;
  questions: Question[];
};

export default function AnswerQuiz() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [quiz, setQuiz] = useState<Quiz | null>(null);
  const [answers, setAnswers] = useState<Record<number, string | string[]>>({});

  useEffect(() => {
    fetch(`http://localhost:8080/api/quizzes/${id}`)
      .then((res) => res.json())
      .then((data) => setQuiz(data))
      .catch((err) => console.error("Error fetching quiz:", err));
  }, [id]);

  const handleSingle = (qId: number, option: string) => {
    setAnswers((prev) => ({ ...prev, [qId]: option }));
  };

  const handleMulti = (qId: number, option: string) => {
    const existing = (answers[qId] as string[]) || [];
    const updated = existing.includes(option)
      ? existing.filter((x) => x !== option)
      : [...existing, option];

    setAnswers((prev) => ({ ...prev, [qId]: updated }));
  };

  const handleSubmit = async () => {
    try {
      const response = await fetch(
        `http://localhost:8080/api/quizzes/${id}/submit`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(answers),
        }
      );

      if (!response.ok) {
        throw new Error("Failed to submit answers");
      }

      const result = await response.json();

      navigate(`/quiz/${id}/result`, {
        state: {
          result: result, // backend response
          answers: answers,
          quiz: quiz,
        },
      });
    } catch (err) {
      console.error(err);
      alert("Failed to submit answers.");
    }
  };

  if (!quiz) return <div>Loading...</div>;

  return (
    <div style={{ padding: 20 }}>
      <button onClick={() => navigate(-1)}>← Back</button>

      <h1>{quiz.title}</h1>

      {quiz.questions.map((q) => (
        <div key={q.id} style={{ marginBottom: 20 }}>
          <h3>{q.text}</h3>

          {q.options.map((opt) => {
            const name = `q-${q.id}`;

            const checked =
              q.type === "single"
                ? answers[q.id] === opt
                : (answers[q.id] as string[])?.includes(opt);

            return (
              <label key={opt} style={{ display: "block" }}>
                <input
                  type={q.type === "single" ? "radio" : "checkbox"}
                  name={name}
                  checked={checked}
                  onChange={() =>
                    q.type === "single"
                      ? handleSingle(q.id, opt)
                      : handleMulti(q.id, opt)
                  }
                />
                {opt}
              </label>
            );
          })}
        </div>
      ))}

      <button onClick={handleSubmit} style={{ marginTop: 20 }}>
        Submit Answers
      </button>
    </div>
  );
}
