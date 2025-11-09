import React from 'react';
import TeacherDashboard from './components/TeacherDashboard'; // No extension needed
import './App.css';

function App() {
  return (
    <div className="App">
      <TeacherDashboard teacherId={1} />
    </div>
  );
}

export default App;