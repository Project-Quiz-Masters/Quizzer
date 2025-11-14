-- === SAMPLE QUIZ ===
INSERT INTO QUIZ (ID, TITLE, DESCRIPTION, COURSE, PUBLISHED)
VALUES (1, 'The Scrum framework',
        'Learn about the roles, events and artifacts of the Scrum framework',
        'SOF005AS3AE', TRUE);

-- === SAMPLE QUESTIONS ===
INSERT INTO QUESTION (ID, TEXT, DIFFICULTY, QUIZ_ID)
VALUES (1, 'What is the purpose of the Retrospective event?', 'Easy', 1),
       (2, 'What is the responsibility of the Scrum Master?', 'Hard', 1);

-- === SAMPLE ANSWER OPTIONS ===
INSERT INTO ANSWER_OPTION (ID, TEXT, CORRECT, QUESTION_ID)
VALUES (1, 'Planning the requirements for the upcoming Sprint', FALSE, 1),
       (2, 'Finding ways to improve the process', TRUE, 1),
       (3, 'Tracking the progress of the Sprint', FALSE, 1),
       (4, 'Ensuring Scrum is understood and enacted', TRUE, 2),
       (5, 'Writing the code for the product', FALSE, 2);
ALTER TABLE QUESTION ALTER COLUMN ID RESTART WITH 3;
