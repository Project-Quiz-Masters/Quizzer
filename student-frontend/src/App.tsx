import { Routes, Route } from "react-router-dom";
import QuizList from "./pages/QuizList";
import AnswerQuiz from "./pages/AnswerQuiz";
import QuizResult from "./pages/QuizResult";

export default function App() {
  return (
    <Routes>
      <Route path="/" element={<QuizList />} />
      <Route path="/quiz/:id" element={<AnswerQuiz />} />
      <Route path="/quiz/:id/result" element={<QuizResult />} />
    </Routes>
  );
}
