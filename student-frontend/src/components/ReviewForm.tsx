import "../App.css";
import { useState } from "react";
import { createReview } from "../services/reviewService";

interface ReviewFormProps {
  quizId: number;
  isPublished: boolean;
}

export default function ReviewForm({ quizId }: ReviewFormProps) {
  const [nickname, setNickname] = useState("");
  const [rating, setRating] = useState<number | null>(null);
  const [text, setText] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setError("");
    setSuccess("");

    if (!nickname || !rating || !text) {
      setError("All fields are required.");
      return;
    }

    try {
      await createReview(quizId, { nickname, rating, text });
      setSuccess("Review submitted successfully!");
      setNickname("");
      setRating(null);
      setText("");
    } catch (err: any) {
      setError(err.message || "Failed to submit review");
    }

  }

  return (


    <div className="page-container review-page">
      <div className="card review-form-card">

        {error && <p className="error-text">{error}</p>}
        {success && <p className="success-text">{success}</p>}

        <form onSubmit={handleSubmit} className="form-vertical">
          <div className="form-group">
            <label htmlFor="nickname">Nickname *</label>
            <input
              id="nickname"
              type="text"
              value={nickname}
              onChange={(e) => setNickname(e.target.value)}
              className="input-field"
            />
          </div>

          <div className="form-group">
            <label>Rating *</label>
            <div className="radio-group-vertical">
              {[
                { value: 1, label: "Useless" },
                { value: 2, label: "Poor" },
                { value: 3, label: "Ok" },
                { value: 4, label: "Good" },
                { value: 5, label: "Excellent" },
              ].map((item) => (
                <label key={item.value} className="radio-label-vertical">
                  <input
                    type="radio"
                    name="rating"
                    value={item.value}
                    checked={rating === item.value}
                    onChange={() => setRating(item.value)}
                  />
                  <span>{item.value} - {item.label}</span>
                </label>
              ))}
            </div>
          </div>

          <div className="form-group">
            <label htmlFor="reviewText">Review *</label>
            <textarea
              id="reviewText"
              value={text}
              onChange={(e) => setText(e.target.value)}
              rows={6}
              className="textarea-field"
            />
          </div>

          <button type="submit" className="submit-button">
            Submit Review
          </button>
        </form>
      </div>
    </div>

  );
}