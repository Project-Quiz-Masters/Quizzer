import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { getAllCategories, type Category } from "../services/categoryService";

function CategoriesList() {
  const [categories, setCategories] = useState<Category[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        setLoading(true);
        const data = await getAllCategories();
        setCategories(data);
        setError(null);
      } catch (err) {
        setError(err instanceof Error ? err.message : "Failed to load categories");
      } finally {
        setLoading(false);
      }
    };

    fetchCategories();
  }, []);

  if (loading) {
    return <div className="loading">Loading categories...</div>;
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
      <h1 className="page-title">Categories</h1>

      {categories.length === 0 ? (
        <p>No categories available yet.</p>
      ) : (
        <div className="card">
          <table className="quiz-table">
            <thead>
              <tr>
                <th>Name</th>
                <th>Description</th>
              </tr>
            </thead>
            <tbody>
              {categories.map((category) => (
                <tr key={category.id}>
                  <td>
                    <Link to={`/categories/${category.id}/quizzes`} className="quiz-link">
                      {category.name}
                    </Link>
                  </td>
                  <td>{category.description || "-"}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}

export default CategoriesList;
