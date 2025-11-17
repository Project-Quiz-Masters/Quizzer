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
type BackendQuestion = {
  id: number;
  text: string;
  options: string[];
  correct?: string[];
};
export default function AnswerQuiz() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [quiz, setQuiz] = useState<Quiz | null>(null);
  const [answers, setAnswers] = useState<Record<number, string | string[]>>({});

  useEffect(() => {
    fetch(`http://localhost:8080/api/quizzes/public/${id}`)
      .then(async (res) => {
        if (!res.ok) throw new Error("Quiz not found");
        return res.json();
      })
      .then((data) => {
        const quizObj = Array.isArray(data) ? data[0] : data;

        if (!quizObj) return;

        // Convert backend "options" → frontend "answerOptions"
        quizObj.questions = quizObj.questions.map((q: BackendQuestion) => ({
          ...q,
          answerOptions: (q.options ?? []).map(
            (text: string, index: number) => ({
              id: index + 1,
              text: text,
              correct: q.correct?.includes(text) ?? false,
            })
          ),
          type: (q.correct?.length ?? 0) > 1 ? "multiple" : "single",
        }));

        setQuiz(quizObj);
      })

      .catch((err) => console.error("Error fetching quiz:", err));
  }, [id]);

  // SINGLE CHOICE
  const handleSingle = (qId: number, option: string) => {
    setAnswers((prev) => ({ ...prev, [qId]: option }));
  };

  // MULTIPLE CHOICE
  const handleMulti = (qId: number, option: string) => {
    const existing = (answers[qId] as string[]) || [];
    const updated = existing.includes(option)
      ? existing.filter((o) => o !== option)
      : [...existing, option];
    setAnswers((prev) => ({ ...prev, [qId]: updated }));
  };

  const handleSubmit = async () => {
    const unanswered = quiz?.questions.filter((q) => {
      const ans = answers[q.id];
      return !ans || (Array.isArray(ans) && ans.length === 0);
    });

    if (unanswered && unanswered.length > 0) {
      alert("Please answer all questions before submitting!");
      return;
    }

    const formatted = Object.fromEntries(
      Object.entries(answers).map(([qId, value]) => [
        qId,
        Array.isArray(value) ? value : [value],
      ])
    );
    try {
      const response = await fetch(
        `http://localhost:8080/api/quizzes/${id}/submit`,
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(formatted),
        }
      );

      if (!response.ok) throw new Error("Submit failed");
      const result = await response.json();

      navigate(`/quiz/${id}/result`, {
        state: { result, answers, quiz },
      });
    } catch (err) {
      alert("Failed to submit answers.");
      console.error(err);
    }
  };

  if (!quiz) return <div>Loading...</div>;

  return (
    <div
      style={{
        padding: 20,
        maxWidth: 700,
        margin: "0 auto",
        fontFamily: "Arial, sans-serif",
      }}
    >
      <button
        onClick={() => navigate(-1)}
        style={{
          padding: "10px 18px",
          marginBottom: "20px",
          background: "#f3f4f6",
          border: "1px solid #ddd",
          borderRadius: "8px",
          cursor: "pointer",
        }}
      >
        ← Back
      </button>
      <h1 style={{ fontSize: "36px", fontWeight: "700", marginBottom: "30px" }}>
        {quiz.title}
      </h1>

      {quiz.questions.map((q) => (
        <div
          key={q.id}
          style={{
            padding: "20px",
            marginBottom: "30px",
            borderRadius: "12px",
            border: "1px solid #ddd",
            background: "#fff",
            boxShadow: "0 2px 8px rgba(0,0,0,0.05)",
          }}
        >
          <h3
            style={{
              fontSize: "18px",
              fontWeight: "600",
              marginBottom: "15px",
            }}
          >
            {q.text}
          </h3>

          {q.answerOptions.map((opt) => {
            const name = `q-${q.id}`;
            const isSingle = q.type === "single";

            const checked = isSingle
              ? answers[q.id] === opt.text
              : (answers[q.id] as string[])?.includes(opt.text);

            return (
              <label
                key={opt.id}
                style={{
                  display: "flex",
                  alignItems: "center",
                  gap: "12px",
                  padding: "10px",
                  marginBottom: "8px",
                  borderRadius: "8px",
                  cursor: "pointer",
                  transition: "0.2s",
                  background: checked ? "#e0f2fe" : "transparent",
                  border: checked ? "1px solid #3b82f6" : "1px solid #ddd",
                }}
              >
                <input
                  type={isSingle ? "radio" : "checkbox"}
                  name={name}
                  checked={!!checked}
                  onChange={() =>
                    isSingle
                      ? handleSingle(q.id, opt.text)
                      : handleMulti(q.id, opt.text)
                  }
                  style={{
                    width: "20px",
                    height: "20px",
                    cursor: "pointer",
                    accentColor: "#3b82f6",
                  }}
                />
                <span style={{ fontSize: "16px" }}>{opt.text}</span>
              </label>
            );
          })}
        </div>
      ))}

      <button
        onClick={handleSubmit}
        style={{
          marginTop: "20px",
          padding: "12px 24px",
          background: "#2563eb",
          color: "white",
          fontSize: "16px",
          border: "none",
          borderRadius: "8px",
          cursor: "pointer",
          transition: "0.2s",
        }}
      >
        Submit Answers
      </button>
    </div>
  );
}
