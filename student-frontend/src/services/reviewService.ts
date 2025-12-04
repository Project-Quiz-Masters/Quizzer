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

// Updates an existing review by ID using the backend REST API.
export interface UpdateReviewRequest {
  nickname: string;
  rating: number;
  text: string;
}

export async function updateReview(
  reviewId: number,
  review: UpdateReviewRequest
) {
  const response = await fetch(
    `${import.meta.env.VITE_BACKEND_URL}/api/reviews/${reviewId}`,
    {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(review),
    }
  );

  if (!response.ok) {
    // Error handling
    let errorMessage = "Failed to update review";
    try {
      const errorBody = await response.json();
      if (errorBody?.message) {
        errorMessage = errorBody.message;
      }
    } catch {
      // Ignore JSON parsing errors
    }
    throw new Error(errorMessage);
  }

  return response.json();
}




export async function getReviewsByQuizId(quizId: number) {
  const response = await fetch(
    `${import.meta.env.VITE_BACKEND_URL}/api/quizzes/${quizId}/reviews`
  );

  if (!response.ok) {
    throw new Error("Failed to fetch reviews");
  }

  return response.json();
}
