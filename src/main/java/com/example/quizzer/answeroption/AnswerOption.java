
package com.example.quizzer.answeroption;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.example.quizzer.question.Question;
import jakarta.persistence.*;

@Entity
public class AnswerOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;
    private boolean correct;

    @JsonBackReference
    @ManyToOne(optional = false)
    @JoinColumn(name = "question_id")
    private Question question;

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getText() {return text;}
    public void setText(String text) {this.text = text;}

    public Boolean isCorrect() {return correct;}
    public void setCorrect(Boolean correct) {this.correct = correct;}

    public Question getQuestion() {return question;}
    public void setQuestion(Question question) {this.question = question;}
}
