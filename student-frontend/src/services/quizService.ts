const BACKEND_URL = "http://localhost:8080";

export interface Quiz {
  id: number;
  name: string;
  description: string;
  courseCode: string;   // adjust if your backend uses different name
  categoryName: string; // adjust if needed
  createdAt: string;    // date string
  published: boolean;
  questionCount?: number;
}

export interface Question {
  id: number;
  text: string;
  difficulty: string;
  answerOptions?: AnswerOption[];
}

export interface AnswerOption {
  id: number;
  text: string;
  isCorrect: boolean;
}

function handleJsonResponse<T>(response: Response): Promise<T> {
  if (!response.ok) {
    return response.json().then((err) => {
      const message = err?.message || `Request failed with status ${response.status}`;
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
