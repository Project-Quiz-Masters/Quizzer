const BACKEND_URL = "http://localhost:8080";

export interface Category {
  id: number;
  name: string;
  description?: string;
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

// Get all categories
export async function getAllCategories(): Promise<Category[]> {
  const res = await fetch(`${BACKEND_URL}/api/categories`);
  return handleJsonResponse<Category[]>(res);
}

// Get single category by id
export async function getCategoryById(id: number): Promise<Category> {
  const res = await fetch(`${BACKEND_URL}/api/categories/${id}`);
  return handleJsonResponse<Category>(res);
}

// Get published quizzes for a category
export async function getQuizzesByCategory(categoryId: number): Promise<any[]> {
  const res = await fetch(`${BACKEND_URL}/api/categories/${categoryId}/quizzes`);
  return handleJsonResponse<any[]>(res);
}
