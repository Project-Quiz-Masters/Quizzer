import { Routes, Route, Link, Navigate } from "react-router-dom";
import QuizzesList from "./pages/QuizzesList";
import QuizDetails from "./pages/QuizDetails.tsx";
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
          </nav>
        </div>
      </header>

      <main className="app-content">
        <Routes>
          <Route path="/" element={<Navigate to="/quizzes" />} />
          <Route path="/quizzes" element={<QuizzesList />} />
          <Route path="/quizzes/:id" element={<QuizDetails />} />
        </Routes>
      </main>
    </div>
  );
}

export default App;
