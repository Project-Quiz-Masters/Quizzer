const API_BASE_URL = import.meta.env.VITE_BACKEND_URL || 'http://localhost:8080';

console.log('API_BASE_URL:', API_BASE_URL); // Add this for debugging

export interface AnswerOption {
  id: number;
  text: string;
  correct: boolean;
}

export interface Question {
  id: number;
  text: string;
  difficulty: string;
  answerOptions?: AnswerOption[];
}

export interface Quiz {
  id: number;
  name: string;
  description: string;
  courseCode: string;
  categoryName: string | null;
  createdAt: string;
  published: boolean;
  questions?: Question[];
}

export async function getAllPublishedQuizzes(): Promise<Quiz[]> {
  const response = await fetch(`${API_BASE_URL}/api/quizzes`);
  if (!response.ok) {
    throw new Error("Failed to fetch quizzes");
  }
  return response.json();
}

export async function getQuizById(id: number): Promise<Quiz> {
  const response = await fetch(`${API_BASE_URL}/api/quizzes/${id}`);
  if (!response.ok) {
    throw new Error("Quiz not found");
  }
  return response.json();
}
