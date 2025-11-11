import React, { useState } from "react";

// Props definition for better type safety and IntelliSense
interface AnswerOptionDeleteProps {
  answerOptionId: number;
  onDeleteSuccess?: (id: number) => void;
  answerText: string;
}

const AnswerOptionDelete: React.FC<AnswerOptionDeleteProps> = ({
  answerOptionId,
  onDeleteSuccess,
  answerText
}) => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  async function handleDelete() {
    if (!window.confirm("Are you sure you want to delete this answer option?")) return;

    setLoading(true);
    setError("");

    try {
      const response = await fetch(`/api/answer-options/${answerOptionId}`, {
        method: "DELETE"
      });

      if (!response.ok) {
        throw new Error("Error deleting answer option");
      }

      setLoading(false);
      alert("Answer option deleted!");
      if (onDeleteSuccess) onDeleteSuccess(answerOptionId);
    } catch (e: any) {
      setLoading(false);
      setError(e.message || "Unknown error");
    }
  }

  return (
    <div style={{ marginBottom: 8 }}>
      <span>{answerText}</span>
      <button
        onClick={handleDelete}
        disabled={loading}
        style={{ marginLeft: 8 }}
      >
        {loading ? "Deleting..." : "Delete"}
      </button>
      {error && (
        <div style={{ color: "red", marginTop: 4 }}>{error}</div>
      )}
    </div>
  );
};

export default AnswerOptionDelete;
