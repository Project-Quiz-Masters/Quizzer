import React, { useState, useEffect } from 'react';
import EditQuizModal from './EditQuizModal';

interface Quiz {
  id: number;
  title: string;
  description: string;
  course: string;
  published: boolean;
  teacherId: number;
}

const TeacherDashboard: React.FC<{ teacherId: number }> = ({ teacherId }) => {
  const [quizzes, setQuizzes] = useState<Quiz[]>([]);
  const [message, setMessage] = useState('');
  const [editingQuiz, setEditingQuiz] = useState<Quiz | null>(null);
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);

  useEffect(() => {
    fetchQuizzes();
  }, [teacherId]);

  const fetchQuizzes = async () => {
    try {
      const response = await fetch(`http://localhost:8080/api/quizzes/teacher/${teacherId}`);
      const data = await response.json();
      setQuizzes(data);
    } catch (error) {
      console.error('Error fetching quizzes:', error);
    }
  };

  const handleDeleteQuiz = (quizId: number, quizTitle: string) => {
    if (window.confirm(`Are you sure you want to delete "${quizTitle}"? This action cannot be undone.`)) {
      deleteQuiz(quizId);
    }
  };

  const deleteQuiz = async (quizId: number) => {
    try {
      const response = await fetch(`http://localhost:8080/api/quizzes/${quizId}`, {
        method: 'DELETE',
        headers: {
          'X-Teacher-Id': teacherId.toString(),
          'Content-Type': 'application/json'
        }
      });

      if (response.ok) {
        setMessage('Quiz deleted successfully!');
        fetchQuizzes();
        setTimeout(() => setMessage(''), 3000);
      } else {
        setMessage('Error deleting quiz');
      }
    } catch (error) {
      console.error('Error deleting quiz:', error);
      setMessage('Error deleting quiz');
    }
  };

  const createTestQuiz = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/quizzes', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          title: 'Test Quiz ' + Date.now(),
          description: 'This is a test quiz',
          teacherId: teacherId,
          course: 'Test Course',
          published: false
        })
      });
      if (response.ok) {
        fetchQuizzes();
        setMessage('Test quiz created!');
        setTimeout(() => setMessage(''), 3000);
      }
    } catch (error) {
      console.error('Error creating quiz:', error);
    }
  };

  // CORRECTED: Accepts the entire quiz object
  const handleEditQuiz = (quiz: Quiz) => {
    setEditingQuiz(quiz);
    setIsEditModalOpen(true);
  };

  const handleUpdateQuiz = (updatedQuiz: Quiz) => {
    setQuizzes(prev => prev.map(q => q.id === updatedQuiz.id ? updatedQuiz : q));
    setMessage('Quiz updated successfully!');
    setTimeout(() => setMessage(''), 3000);
  };

  return (
    <div className="teacher-dashboard">
      <h2>My Quizzes</h2>
      
      <button onClick={createTestQuiz} className="btn-create">
        Create Test Quiz
      </button>
      
      {message && (
        <div className={`message ${message.includes('successfully') || message.includes('created') ? 'success' : 'error'}`}>
          {message}
        </div>
      )}

      <div className="quizzes-list">
        {quizzes.length === 0 ? (
          <p>No quizzes created yet. Click "Create Test Quiz" to add one.</p>
        ) : (
          quizzes.map(quiz => (
            <div key={quiz.id} className="quiz-card">
              <h3>{quiz.title} {quiz.published && <span className="published-badge">Published</span>}</h3>
              <p>{quiz.description}</p>
              <p><strong>Course:</strong> {quiz.course}</p>
              <div className="quiz-actions">
                {/* CORRECTED: Pass the entire quiz object */}
                <button 
                  className="btn-edit"
                  onClick={() => handleEditQuiz(quiz)}
                >
                  Edit
                </button>
                <button 
                  className="btn-delete"
                  onClick={() => handleDeleteQuiz(quiz.id, quiz.title)}
                >
                  Delete
                </button>
              </div>
            </div>
          ))
        )}
      </div>

      <EditQuizModal
        quiz={editingQuiz}
        isOpen={isEditModalOpen}
        onClose={() => setIsEditModalOpen(false)}
        onUpdate={handleUpdateQuiz}
      />
    </div>
  );
};

export default TeacherDashboard;