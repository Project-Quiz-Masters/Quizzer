import "../App.css";
import { useState } from "react";
import {
  updateReview,
  type UpdateReviewRequest,
} from "../services/reviewService";
import type { Review } from "../pages/QuizReviewPage";

export interface EditReviewFormProps {
  review: Review;
  onSaved: (updated: Review) => void;
  onCancel: () => void;
}

export default function EditReviewForm({
  review,
  onSaved,
  onCancel,
}: EditReviewFormProps) {
  const [nickname, setNickname] = useState(review.nickname);
  const [rating, setRating] = useState<number | null>(review.rating);
  const [text, setText] = useState(review.text);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [saving, setSaving] = useState(false);

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setError("");
    setSuccess("");

    if (!nickname || !rating || !text) {
      setError("All fields are required.");
      return;
    }

    try {
      setSaving(true);

      const payload: UpdateReviewRequest = {
        nickname,
        rating,
        text,
      };

      const updated = await updateReview(review.id, payload);
      setSuccess("Review updated successfully!");

      onSaved(updated);
    } catch (err: unknown) {
      if (err instanceof Error) {
        setError(err.message);
      } else {
        setError("Failed to update review");
      }
    } finally {
      setSaving(false);
    }
  }

  return (
    <div className="page-container review-page">
      <div className="card review-form-card">
        {error && <p className="error-text">{error}</p>}
        {success && <p className="success-text">{success}</p>}

        <form onSubmit={handleSubmit} className="form-vertical">
          <div className="form-group">
            <label htmlFor="edit-nickname">Nickname *</label>
            <input
              id="edit-nickname"
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
                    name="edit-rating"
                    value={item.value}
                    checked={rating === item.value}
                    onChange={() => setRating(item.value)}
                  />
                  <span>
                    {item.value} - {item.label}
                  </span>
                </label>
              ))}
            </div>
          </div>

          <div className="form-group">
            <label htmlFor="edit-reviewText">Review *</label>
            <textarea
              id="edit-reviewText"
              value={text}
              onChange={(e) => setText(e.target.value)}
              rows={6}
              className="textarea-field"
            />
          </div>

          <div className="submit-section">
            <button type="submit" className="submit-button" disabled={saving}>
              {saving ? "Saving..." : "Save changes"}
            </button>
            <button
              type="button"
              className="review-cancel-button"
              onClick={onCancel}
            >
              Cancel
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
