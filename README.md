# Quizzer

**Quizzer** is a web-based quiz application designed for teachers and students at Haaga-Helia.  
Teachers can create and manage quizzes, questions, and categories through a teacher dashboard.  
Students can take published quizzes, get instant feedback, and write reviews in the student dashboard.  
The goal of the project is to make learning more interactive and provide a practical software development experience for the team.

## Team members

- [Maab Osman](https://github.com/maabosman)
- [Bhavi Kataria](https://github.com/BhaviK06)
- [Ali Eray Demir](https://github.com/alieraydemir080504)
- [Prashoon Jha](https://github.com/prashoonjha)
- [Emanuela](https://github.com/emalle)
- [Mohammad Hanif Arghushi](https://github.com/MohammadHanifArghushi)
- [An Le](https://github.com/chopiean)
- [Lam Ting Hin Osman](https://github.com/osmanlam)
  
## Additional Information

**Team name:** QuizMasters  
**Team Size:** 6-8  
**Communication platforms:** Microsoft Teams & WhatsApp  
**Tech stack:** Backend – Java (Spring Boot), Frontend – HTML, CSS, React

This is Hanif and I wrote this text to check my access to the repository

Prashoon Jha added team size

## Backlog
The Github projects backlog can be accessed through [This link](https://github.com/orgs/Project-Quiz-Masters/projects/5/views/1)

## Developer Guide

### Requirements
- **Java version:** 21  
- **Framework:** Spring Boot (Maven project)
- **Database:** H2 (in-memory)
- Make sure you have **Git**, **Java 21**, and **Maven** installed on your computer.

### Getting Started
To run the backend application from the command line:

1. Clone the repository:
   ```bash
   git clone https://github.com/Project-Quiz-Masters/Quizzer.git
2. Make sure you are on the right branch:
    ```bash
    git checkout main
    git pull
3. Check that the required dependencies are installed by Maven:
    ```bash
    mvn clean install
4. The application should start at:
    http://localhost:8080
5. The project uses the H2 Database, which runs in-memory and does not require extra setup. You can access the database console from:
    ```bash
    http://localhost:8080/h2-console

**### RAHTI PRODUCTION ENVIRONMENT URL**: https://rahti-quizzer-quizzer-postgres.2.rahtiapp.fi/quizzes
**### LINK TO TEH SWAGGER DOCUMENTATION DEPLOYED TO THE PRODUCTION ENVIRONMENT IN RAHTI**: link to the Swagger documentation deployed to the production environment in Rahti

### ***Flinga board link: https://edu.flinga.fi/s/EXCBP45***

**### ER Diagram**

```mermaid
erDiagram
    CATEGORY {
        int ID
        string Title
        string Description
    }

    QUIZ {
        int ID
        string Title
        string Description
        boolean Published
        string CreatedAt
    }

    QUESTION {
        int ID
        string Text
        string Difficulty
    }

    ANSWEROPTION {
        int ID
        string Text
        boolean Correct
    }

    STUDENTANSWER {
        int ID
    }

    CATEGORY ||--o{ QUIZ : contains
    QUIZ ||--o{ QUESTION : includes
    QUESTION ||--o{ ANSWEROPTION
    ANSWEROPTION ||--o{ STUDENTANSWER
```
    




