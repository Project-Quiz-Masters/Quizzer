const BACKEND_URL = "http://localhost:8080";

export interface Quiz {
  id: number;
  title: string;
  description: string;
  course: string;
  createdAt: string;
  published: boolean;
  teacherId: number;
  category?: {
    id: number;
    name: string;
  };
  // Category name provided by DTO endpoints
  categoryName?: string;
}

export interface Question {
  id: number;
  text: string;
  difficulty: string;
  answerOptions: AnswerOption[];
}

export interface AnswerOption {
  id: number;
  text: string;
  correct: boolean;
}

function handleJsonResponse<T>(response: Response): Promise<T> {
  if (!response.ok) {
    return response.json().then((err) => {
      const message =
        err?.message || `Request failed with status ${response.status}`;
      throw new Error(message);
    });
  }
  return response.json();
}

// 👉 Get all published quizzes
export async function getAllPublishedQuizzes(): Promise<Quiz[]> {
  const res = await fetch(`${BACKEND_URL}/api/quizzes`);
  return handleJsonResponse<Quiz[]>(res);
  // If you have /api/quizzes/published, use that instead:
  // const res = await fetch(`${BACKEND_URL}/api/quizzes/published`);
}

// 👉 Get single quiz by id
export async function getQuizById(id: number): Promise<Quiz> {
  const res = await fetch(`${BACKEND_URL}/api/quizzes/${id}`);
  return handleJsonResponse<Quiz>(res);
}

// 👉 Get questions of a quiz
export async function getQuestionsByQuizId(id: number): Promise<Question[]> {
  const res = await fetch(`${BACKEND_URL}/api/quizzes/${id}/questions`);
  return handleJsonResponse<Question[]>(res);
}
// 👉 Submit quiz answers
export interface SubmitQuizAnswer {
  questionId: number;
  answerOptionId: number | null;
}

export interface SubmitQuizRequest {
  quizId: number;
  answers: SubmitQuizAnswer[];
}

export interface SubmitQuizResponse {
  quizId: number;
  correctCount: number;
  totalQuestions: number;
  // you can add more fields if backend sends them
}

export async function submitQuizAnswers(
  payload: SubmitQuizRequest
): Promise<SubmitQuizResponse> {
  const res = await fetch(`${BACKEND_URL}/api/student-answers/submit`, {
  method: "POST",
  headers: { "Content-Type": "application/json" },
  body: JSON.stringify(payload),
});


  if (!res.ok) {
    const txt = await res.text().catch(() => "");
    throw new Error(txt || "Failed to submit quiz");
  }

  return res.json();
}
