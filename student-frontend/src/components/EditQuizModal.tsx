import React, { useState, useEffect } from 'react';

interface Quiz {
  id: number;
  title: string;
  description: string;
  course: string;
  published: boolean;
  teacherId: number;
}

interface EditQuizModalProps {
  quiz: Quiz | null;
  isOpen: boolean;
  onClose: () => void;
  onUpdate: (updatedQuiz: Quiz) => void;
}

const EditQuizModal: React.FC<EditQuizModalProps> = ({ quiz, isOpen, onClose, onUpdate }) => {
  const [formData, setFormData] = useState<Partial<Quiz>>({});
  const [message, setMessage] = useState('');

  useEffect(() => {
    if (quiz) {
      setFormData({
        title: quiz.title,
        description: quiz.description,
        course: quiz.course,
        published: quiz.published
      });
    }
  }, [quiz]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!quiz) return;

    try {
      const response = await fetch(`http://localhost:8080/api/quizzes/${quiz.id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          ...formData,
          teacherId: quiz.teacherId
        })
      });

      if (response.ok) {
        const updatedQuiz = await response.json();
        onUpdate(updatedQuiz);
        setMessage('Quiz updated successfully!');
        setTimeout(() => {
          setMessage('');
          onClose();
        }, 1500);
      } else {
        setMessage('Error updating quiz');
      }
    } catch (error) {
      console.error('Error updating quiz:', error);
      setMessage('Error updating quiz');
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
    const { name, value, type } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? (e.target as HTMLInputElement).checked : value
    }));
  };

  if (!isOpen) return null;

  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <div className="modal-header">
          <h3>Edit Quiz</h3>
          <button className="close-button" onClick={onClose}>×</button>
        </div>
        
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="title">Quiz Title:</label>
            <input
              type="text"
              id="title"
              name="title"
              value={formData.title || ''}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="description">Description:</label>
            <textarea
              id="description"
              name="description"
              value={formData.description || ''}
              onChange={handleChange}
              rows={3}
            />
          </div>

          <div className="form-group">
            <label htmlFor="course">Course Code:</label>
            <input
              type="text"
              id="course"
              name="course"
              value={formData.course || ''}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-group checkbox-group">
            <label>
              <input
                type="checkbox"
                name="published"
                checked={formData.published || false}
                onChange={handleChange}
              />
              Published
            </label>
          </div>

          {message && (
            <div className={`message ${message.includes('successfully') ? 'success' : 'error'}`}>
              {message}
            </div>
          )}

          <div className="modal-actions">
            <button type="button" onClick={onClose} className="btn-cancel">
              Cancel
            </button>
            <button type="submit" className="btn-save">
              Save Changes
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default EditQuizModal;