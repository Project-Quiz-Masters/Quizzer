export interface CreateReviewRequest {
  nickname: string;
  rating: number;
  text: string;
}

export async function createReview(
  quizId: number,
  review: CreateReviewRequest
) {
  const response = await fetch(
    `${import.meta.env.VITE_BACKEND_URL}/api/quizzes/${quizId}/reviews`,
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(review),
    }
  );

  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.message || "Failed to create review");
  }

  return response.json();
}
