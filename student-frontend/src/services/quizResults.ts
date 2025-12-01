export type QuestionResult = {
  questionId: number;
  questionText: string;
  questionDifficulty: string;
  totalAnswers: number;
  correctAnswers: number;
  wrongAnswers: number;
};

export class QuizResultsError extends Error {
  status?: number;
  constructor(message: string, status?: number) {
    super(message);
    this.status = status;
  }
}

// Prefer environment variable, but default to Rahti backend for production demos
const API_URL = (import.meta as any).env?.VITE_API_URL ?? "https://rahti-quizzer-quizzer-postgres.2.rahtiapp.fi";

export async function getQuizResults(quizId: number): Promise<QuestionResult[]> {
  let res: Response;
  try {
    res = await fetch(`${API_URL}/api/student-answers/quiz/${quizId}/results`);
  } catch (e) {
    throw new QuizResultsError("Network error while fetching quiz results");
  }

  if (!res.ok) {
    if (res.status === 404) {
      throw new QuizResultsError("Quiz not found", 404);
    }
    throw new QuizResultsError(`Failed to load quiz results (status ${res.status})`, res.status);
  }

  const data = await res.json();
  return Array.isArray(data) ? data : [];
}

type AnswerSubmission = { questionId: number; answerOptionId: number };

export async function submitQuiz(
  quizId: number,
  answers: AnswerSubmission[]
): Promise<{ quizId: number; correctCount: number; totalQuestions: number }> {
  let res: Response;
  try {
    res = await fetch(`${API_URL}/api/student-answers/submit`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ quizId, answers }),
    });
  } catch (e) {
    throw new QuizResultsError("Network error while submitting quiz");
  }

  if (!res.ok) {
    throw new QuizResultsError(`Failed to submit quiz (status ${res.status})`, res.status);
  }

  return res.json();
}
