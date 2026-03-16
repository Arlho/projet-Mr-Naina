-- Create database (run this separately if needed: CREATE DATABASE resaka_note;)

-- Drop tables if they exist (in reverse dependency order)
DROP TABLE IF EXISTS note CASCADE;
DROP TABLE IF EXISTS parametre CASCADE;
DROP TABLE IF EXISTS resolution CASCADE;
DROP TABLE IF EXISTS operateur CASCADE;
DROP TABLE IF EXISTS correcteur CASCADE;
DROP TABLE IF EXISTS matiere CASCADE;
DROP TABLE IF EXISTS candidat CASCADE;

-- Core Entities
CREATE TABLE candidat (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    matricule VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE matiere (
    id_matiere SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    coefficient NUMERIC DEFAULT 1
);

CREATE TABLE correcteur (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL
);

CREATE TABLE note (
    id SERIAL PRIMARY KEY,
    id_candidat INTEGER REFERENCES candidat(id) ON DELETE CASCADE,
    id_matiere INTEGER REFERENCES matiere(id_matiere) ON DELETE CASCADE,
    id_correcteur INTEGER REFERENCES correcteur(id) ON DELETE CASCADE,
    valeur_note NUMERIC NOT NULL
);

-- Dynamic Calculation Engine
CREATE TABLE operateur (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(50) NOT NULL,
    symbole VARCHAR(10) NOT NULL
);

CREATE TABLE resolution (
    id SERIAL PRIMARY KEY,
    description TEXT,
    resultat NUMERIC
);

CREATE TABLE parametre (
    id SERIAL PRIMARY KEY,
    id_operateur INTEGER REFERENCES operateur(id) ON DELETE CASCADE,
    id_matiere INTEGER REFERENCES matiere(id_matiere) ON DELETE CASCADE,
    id_resolution INTEGER REFERENCES resolution(id) ON DELETE CASCADE,
    min INT,
    max INT
);

-- Initial Data: Operateurs (Comparison symbols)
INSERT INTO operateur (id, nom, symbole) VALUES 
    (1, 'Inferieur', '<'), 
    (2, 'SuperieurOuEgal', '>='), 
    (3, 'InferieurOuEgal', '<='), 
    (4, 'Superieur', '>');
SELECT setval('operateur_id_seq', 4);

-- Resolutions (Calculation Methods)
INSERT INTO resolution (id, description) VALUES 
    (1, 'Petit'), 
    (2, 'Moyenne'), 
    (3, 'Grand');
SELECT setval('resolution_id_seq', 3);

-- Correcteurs
INSERT INTO correcteur (id, nom) VALUES (1, 'Correcteur1'), (2, 'Correcteur2'), (3, 'Correcteur3');
SELECT setval('correcteur_id_seq', 3);

-- Candidats
INSERT INTO candidat (id, nom, prenom, matricule) VALUES
    (1, 'Candidat1', 'Test', 'C001'),
    (2, 'Candidat2', 'Test', 'C002');
SELECT setval('candidat_id_seq', 2);

-- Matieres
INSERT INTO matiere (id_matiere, nom, coefficient) VALUES
    (1, 'JAVA', 1),
    (2, 'PHP', 1);
SELECT setval('matiere_id_matiere_seq', 2);

-- Parametres (mapping to resolutions: 1=Petit/Avg, 2=Moyenne/Max, 3=Grand/Min)
-- JAVA (id_matiere=1): 
-- Gap 10 (Candidat 1) -> Avg (1)
-- Gap 6 (Candidat 2) -> Max (2)
INSERT INTO parametre (id_operateur, id_matiere, id_resolution, min, max) VALUES
    (2, 1, 1, 10, 100), -- >= 10 : Average (Petit)
    (1, 1, 2, 0, 10);   -- < 10  : Max (Moyenne)

-- PHP (id_matiere=2):
-- Gap 0 (Candidat 1) -> Avg (1)
-- Gap 2 (Candidat 2) -> Min (3)
INSERT INTO parametre (id_operateur, id_matiere, id_resolution, min, max) VALUES
    (3, 2, 1, 0, 0),    -- <= 0 : Average (Petit)
    (4, 2, 3, 0, 100);  -- > 0  : Min (Grand);

-- Notes
INSERT INTO note (id_candidat, id_matiere, id_correcteur, valeur_note) VALUES
    (1, 1, 1, 15), (1, 1, 2, 10), (1, 1, 3, 12),
    (2, 1, 1, 9), (2, 1, 2, 8), (2, 1, 3, 11),
    (1, 2, 1, 10), (1, 2, 2, 10),
    (2, 2, 1, 13), (2, 2, 2, 11);
