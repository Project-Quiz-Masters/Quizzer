import { useEffect, useState } from "react";
import { useParams, useNavigate, Link } from "react-router-dom";
import { getQuizzesByCategory, getCategoryById, type Category } from "../services/categoryService";
import type { Quiz } from "../services/quizService";

function formatDate(dateStr: string): string {
  if (!dateStr) return "";
  const d = new Date(dateStr);
  if (Number.isNaN(d.getTime())) return dateStr;
  return d.toLocaleDateString("fi-FI");
}

function CategoryQuizzes() {
  const { categoryId } = useParams<{ categoryId: string }>();
  const navigate = useNavigate();
  const [category, setCategory] = useState<Category | null>(null);
  const [quizzes, setQuizzes] = useState<Quiz[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchData = async () => {
      if (!categoryId) return;

      try {
        setLoading(true);
        const catId = parseInt(categoryId, 10);

        // Fetch both category details and quizzes
        const [categoryData, quizzesData] = await Promise.all([
          getCategoryById(catId),
          getQuizzesByCategory(catId),
        ]);

        setCategory(categoryData);
        setQuizzes(quizzesData);
        setError(null);
      } catch (err) {
        setError(err instanceof Error ? err.message : "Failed to load quizzes");
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [categoryId]);

  if (loading) {
    return <div className="loading">Loading quizzes...</div>;
  }

  if (error) {
    return (
      <div className="error-container">
        <p className="error-text">Error: {error}</p>
        <p className="error-hint">Make sure your backend is running on http://localhost:8080</p>
      </div>
    );
  }

  return (
    <div className="page-container">
      <button onClick={() => navigate("/categories")} className="back-button">
        ← Back to Categories
      </button>

      <h1 className="page-title">{category?.name || "Category"} Quizzes</h1>
      {category?.description && <p className="quiz-description">{category.description}</p>}

      {quizzes.length === 0 ? (
        <p>No published quizzes in this category yet.</p>
      ) : (
        <div className="card">
          <table className="quiz-table">
            <thead>
              <tr>
                <th>Name</th>
                <th>Description</th>
                <th>Course</th>
                <th>Added on</th>
              </tr>
            </thead>
            <tbody>
              {quizzes.map((quiz) => (
                <tr key={quiz.id}>
                  <td>
                    <Link to={`/quizzes/${quiz.id}`} className="quiz-link">
                      {quiz.title}
                    </Link>
                  </td>
                  <td>{quiz.description || "-"}</td>
                  <td>{quiz.course || "-"}</td>
                  <td>{formatDate(quiz.createdAt)}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}

export default CategoryQuizzes;
