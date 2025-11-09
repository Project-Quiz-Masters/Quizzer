import React from 'react';
import TeacherDashboard from './components/TeacherDashboard'; // No extension needed
import './App.css';
import EditQuizModal from './components/EditQuizModal';

function App() {
  return (
    <div className="App">
      <TeacherDashboard teacherId={1} />
    </div>
  );
}

export default App;