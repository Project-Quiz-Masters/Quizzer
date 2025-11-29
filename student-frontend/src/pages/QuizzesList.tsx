import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { getAllPublishedQuizzes, type Quiz } from "../services/quizService";

function formatDate(dateStr: string): string {
    if (!dateStr) return "";
    const d = new Date(dateStr);
    if (Number.isNaN(d.getTime())) return dateStr;
    return d.toLocaleDateString("fi-FI");
}

export default function QuizzesList() {
    const [quizzes, setQuizzes] = useState<Quiz[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        async function loadQuizzes() {
            try {
                setLoading(true);
                setError(null);
                console.log("Fetching quizzes from API...");
                const data = await getAllPublishedQuizzes();
                console.log("Received data:", data);
                setQuizzes(data);
            } catch (err: any) {
                console.error("Error loading quizzes:", err);
                setError(err.message || "Failed to load quizzes. Is the backend running?");
            } finally {
                setLoading(false);
            }
        }

        loadQuizzes();
    }, []);

    return (
        <div className="page-container">
            <h1 className="page-title">Quizzes</h1>

            {loading && <p>Loading quizzes...</p>}
            {error && (
                <div className="error-container">
                    <p className="error-text">{error}</p>
                    <p className="error-hint">
                        Make sure your backend is running on http://localhost:8080
                    </p>
                </div>
            )}

            {!loading && !error && quizzes.length === 0 && (
                <p>No quizzes available.</p>
            )}

            {!loading && !error && quizzes.length > 0 && (
                <div className="card">
                    <table className="quiz-table">
                        <thead>
                            <tr>
                                <th>Name</th>
                                <th>Description</th>
                                <th>Course</th>
                                <th>Category</th>
                                <th>Added on</th>
                                <th>Results</th>
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
                                    <td>{quiz.description}</td>
                                    <td>{quiz.course}</td>
                                    <td>{(quiz as any).categoryName || "-"}</td>
                                    <td>{formatDate(quiz.createdAt)}</td>
                                    <td>
                                        <Link to={`/quizzes/${quiz.id}/results`} className="quiz-link">
                                            View results
                                        </Link>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            )}
        </div>
    );
}
