import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";

type AnswerOption = {
  id: number;
  text: string;
  correct: boolean;
};

type Question = {
  id: number;
  text: string;
  difficulty: string;
  answerOptions: AnswerOption[];
  type?: "single" | "multiple";
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
    fetch(`http://localhost:8080/api/public/quizzes/${id}`)
      .then(async (res) => {
        if (!res.ok) {
          throw new Error("Quiz not found");
        }
        return res.json();
      })
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

  const formatted = Object.fromEntries(
    Object.entries(answers).map(([qId, value]) => [
      qId,
      Array.isArray(value) ? value : [value],
    ])
  );

  const handleSubmit = async () => {
    try {
      const response = await fetch(
        `http://localhost:8080/api/quizzes/${id}/submit`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(formatted),
        }
      );

      if (!response.ok) {
        throw new Error("Failed to submit answers");
      }

      const result = await response.json();

      navigate(`/quiz/${id}/result`, {
        state: {
          result,
          answers,
          quiz,
        },
      });
    } catch (err) {
      console.error(err);
      alert("Failed to submit answers.");
    }
  };

  // Prevent crash before data loads
  if (!quiz || !quiz.questions) return <div>Loading...</div>;

  return (
    <div style={{ padding: 20 }}>
      <button onClick={() => navigate(-1)}>← Back</button>

      <h1>{quiz.title}</h1>

      {quiz.questions.map((q) => (
        <div key={q.id} style={{ marginBottom: 20 }}>
          <h3>{q.text}</h3>

          {q.answerOptions.map((opt) => {
            const name = `q-${q.id}`;

            const checked =
              q.type === "single"
                ? answers[q.id] === opt.text
                : (answers[q.id] as string[])?.includes(opt.text);

            return (
              <label key={opt.id} style={{ display: "block" }}>
                <input
                  type={q.type === "single" ? "radio" : "checkbox"}
                  name={name}
                  checked={checked}
                  onChange={() =>
                    q.type === "single"
                      ? handleSingle(q.id, opt.text)
                      : handleMulti(q.id, opt.text)
                  }
                />
                {opt.text}
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
