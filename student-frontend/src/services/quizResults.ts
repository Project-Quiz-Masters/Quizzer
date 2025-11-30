

export type QuestionResult = {
  questionId: number;
  questionText: string;
  questionDifficulty: string;
  totalAnswers: number;
  correctAnswers: number;
  wrongAnswers: number;
};

const API_URL = import.meta.env.VITE_API_URL ?? "http://localhost:8080";

export async function getQuizResults(quizId: number): Promise<QuestionResult[]> {
  const res = await fetch(`${API_URL}/api/student-answers/quiz/${quizId}/results`);
  if (!res.ok) throw new Error("Failed to load quiz results");
  return res.json();
}
