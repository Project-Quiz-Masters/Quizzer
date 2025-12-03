import { useState } from "react";
import { createReview } from "../services/reviewService";

interface ReviewFormProps {
  quizId: number;
  isPublished: boolean;
}

export default function ReviewForm({ quizId, isPublished }: ReviewFormProps) {
  const [nickname, setNickname] = useState("");
  const [rating, setRating] = useState<number | null>(null);
  const [text, setText] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setError("");
    setSuccess("");

    // basic validation
    if (!nickname || !rating || !text) {
      setError("All fields are required.");
      return;
    }

    try {
      await createReview(quizId, {
        nickname,
        rating,
        text,
      });

      setSuccess("Review submitted successfully!");
      setNickname("");
      setRating(null);
      setText("");
    } catch (err: any) {
      setError(err.message || "Failed to submit review");
    }
  }

  if (!isPublished) {
    return <p>This quiz is not published. Reviews are disabled.</p>;
  }

  return (
    <div className="card">
      <h2>Add a review</h2>

      {error && <p style={{ color: "red" }}>{error}</p>}
      {success && <p style={{ color: "green" }}>{success}</p>}

      <form onSubmit={handleSubmit}>
        <div>
          <label>Nickname *</label>
          <input
            type="text"
            value={nickname}
            onChange={(e) => setNickname(e.target.value)}
          />
        </div>

        <div style={{ marginTop: "1rem" }}>
          <label>Rating *</label>
          <div>
            {[1, 2, 3, 4, 5].map((num) => (
              <label key={num} style={{ display: "block" }}>
                <input
                  type="radio"
                  name="rating"
                  value={num}
                  checked={rating === num}
                  onChange={() => setRating(num)}
                />{" "}
                {num}
              </label>
            ))}
          </div>
        </div>

        <div style={{ marginTop: "1rem" }}>
          <label>Review *</label>
          <textarea
            value={text}
            onChange={(e) => setText(e.target.value)}
            rows={3}
          />
        </div>

        <button type="submit" style={{ marginTop: "1rem" }}>
          Submit your review
        </button>
      </form>
    </div>
  );
}
