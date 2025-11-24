import { Routes, Route, Link, Navigate } from "react-router-dom";
import QuizzesList from "./pages/QuizzesList";
import QuizDetails from "./pages/QuizDetails.tsx";
import CategoriesList from "./pages/CategoriesList";
import CategoryQuizzes from "./pages/CategoryQuizzes";
import QuizResults from "./pages/QuizResults";

import "./App.css";

function App() {
  return (
    <div className="app-root">
      
      <header className="app-header">
        <div className="app-header-inner">
          <span className="app-logo">Quizzer</span>
          <nav>
            <Link to="/quizzes" className="nav-link">
              QUIZZES
            </Link>
            <Link to="/categories" className="nav-link">
              CATEGORIES
            </Link>
          </nav>
        </div>
      </header>

      <main className="app-content">
        <Routes>
          <Route path="/" element={<Navigate to="/quizzes" />} />
          <Route path="/quizzes" element={<QuizzesList />} />
          <Route path="/quizzes/:id" element={<QuizDetails />} />
          <Route path="/quizzes/:id/results" element={<QuizResults />} />
          <Route path="/categories" element={<CategoriesList />} />
          <Route path="/categories/:categoryId/quizzes" element={<CategoryQuizzes />} />
        </Routes>
      </main>
    </div>
  );
}

export default App;
