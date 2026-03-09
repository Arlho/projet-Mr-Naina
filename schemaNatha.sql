-- Create Grade Management Schema

-- Drop tables in reverse order of creation to avoid foreign key issues
DROP TABLE IF EXISTS grades CASCADE;
DROP TABLE IF EXISTS exams CASCADE;
DROP TABLE IF EXISTS parameters CASCADE;
DROP TABLE IF EXISTS subjects CASCADE;
DROP TABLE IF EXISTS correctors CASCADE;
DROP TABLE IF EXISTS students CASCADE;
DROP TABLE IF EXISTS operators CASCADE;

-- 1. Operators (Used to define how grades are interpreted like "highest" -> '>', "lowest" -> '<', etc.)
CREATE TABLE operators (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL, -- e.g., 'Highest', 'Lowest', 'Average'
    symbol VARCHAR(10)          -- e.g., '>', '<', 'null'
);

-- 2. Students
CREATE TABLE students (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

-- 3. Correctors (Teachers/Administrators)
CREATE TABLE correctors (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

-- 4. Subjects (with Coefficient)
CREATE TABLE subjects (
    id SERIAL PRIMARY KEY,
    subject_name VARCHAR(100) NOT NULL,
    coefficient NUMERIC(5, 2) DEFAULT 1.0
);

-- 5. Exams (Linked to a Subject)
CREATE TABLE exams (
    id SERIAL PRIMARY KEY,
    id_subject INT REFERENCES subjects(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    exam_date DATE DEFAULT CURRENT_DATE
);

-- 6. Parameters (Grade ranges and grading rules per subject)
CREATE TABLE parameters (
    id SERIAL PRIMARY KEY,
    id_subject INT REFERENCES subjects(id) ON DELETE CASCADE,
    min_value NUMERIC(5, 2),
    max_value NUMERIC(5, 2),
    id_operator INT REFERENCES operators(id)
);

-- 7. Grades (Actual scores for students per exam)
CREATE TABLE grades (
    id SERIAL PRIMARY KEY,
    id_student INT REFERENCES students(id) ON DELETE CASCADE,
    id_exam INT REFERENCES exams(id) ON DELETE CASCADE,
    value NUMERIC(5, 2) NOT NULL CHECK (value >= 0),
    id_corrector INT REFERENCES correctors(id)
);
