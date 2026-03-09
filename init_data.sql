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
    id_candidat INTEGER REFERENCES candidat(id),
    id_matiere INTEGER REFERENCES matiere(id_matiere),
    id_correcteur INTEGER REFERENCES correcteur(id),
    valeur_note NUMERIC NOT NULL
);

-- Dynamic Calculation Engine
CREATE TABLE operateur (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(50) NOT NULL,
    symbole VARCHAR(10) NOT NULL
);

CREATE TABLE parametre (
    id SERIAL PRIMARY KEY,
    id_operateur INTEGER REFERENCES operateur(id),
    id_matiere INTEGER REFERENCES matiere(id_matiere),
    min INT,
    max INT
);

CREATE TABLE resolution (
    id SERIAL PRIMARY KEY,
    description TEXT,
    resultat NUMERIC
);

-- Initial Data: Operateurs
INSERT INTO operateur (nom, symbole) VALUES 
    ('Addition', '+'), 
    ('Soustraction', '-'), 
    ('Multiplication', '*'), 
    ('Division', '/');

-- Correcteurs
INSERT INTO correcteur (nom) VALUES ('Louis'), ('Nyaina'), ('Mikolo');

-- Candidats
INSERT INTO candidat (nom, prenom, matricule) VALUES
    ('Rakoto', 'Jean', 'MAT001'),
    ('Rabe', 'Marie', 'MAT002'),
    ('Randria', 'Paul', 'MAT003');

-- Matieres
INSERT INTO matiere (nom, coefficient) VALUES
    ('Mathematiques', 3),
    ('Physique', 2),
    ('Informatique', 4);

-- Parametres (min-max ranges with operators per matiere)
-- Mathematiques (id_matiere=1): small gap = average(*), medium gap = highest(+), large gap = lowest(-)
INSERT INTO parametre (id_operateur, id_matiere, min, max) VALUES
    (3, 1, 0, 5),    -- gap 0-5 : * (average)
    (1, 1, 6, 15),   -- gap 6-15 : + (highest)
    (2, 1, 16, 100);  -- gap 16+ : - (lowest)

-- Physique (id_matiere=2)
INSERT INTO parametre (id_operateur, id_matiere, min, max) VALUES
    (3, 2, 0, 4),    -- gap 0-4 : * (average)
    (1, 2, 5, 10),   -- gap 5-10 : + (highest)
    (2, 2, 11, 100);  -- gap 11+ : - (lowest)

-- Informatique (id_matiere=3)
INSERT INTO parametre (id_operateur, id_matiere, min, max) VALUES
    (3, 3, 0, 6),    -- gap 0-6 : * (average)
    (1, 3, 7, 12),   -- gap 7-12 : + (highest)
    (2, 3, 13, 100);  -- gap 13+ : - (lowest)

-- Notes: Candidat 1 (Rakoto Jean)
INSERT INTO note (id_candidat, id_matiere, id_correcteur, valeur_note) VALUES
    (1, 1, 1, 14),  -- Math, Louis: 14
    (1, 1, 2, 12),  -- Math, Nyaina: 12
    (1, 1, 3, 15),  -- Math, Mikolo: 15
    (1, 2, 1, 10),  -- Physique, Louis: 10
    (1, 2, 2, 16),  -- Physique, Nyaina: 16
    (1, 2, 3, 13),  -- Physique, Mikolo: 13
    (1, 3, 1, 17),  -- Info, Louis: 17
    (1, 3, 2, 18),  -- Info, Nyaina: 18
    (1, 3, 3, 16);  -- Info, Mikolo: 16

-- Notes: Candidat 2 (Rabe Marie)
INSERT INTO note (id_candidat, id_matiere, id_correcteur, valeur_note) VALUES
    (2, 1, 1, 8),   -- Math, Louis: 8
    (2, 1, 2, 15),  -- Math, Nyaina: 15
    (2, 1, 3, 11),  -- Math, Mikolo: 11
    (2, 2, 1, 12),  -- Physique, Louis: 12
    (2, 2, 2, 13),  -- Physique, Nyaina: 13
    (2, 2, 3, 11),  -- Physique, Mikolo: 11
    (2, 3, 1, 9),   -- Info, Louis: 9
    (2, 3, 2, 14),  -- Info, Nyaina: 14
    (2, 3, 3, 7);   -- Info, Mikolo: 7

-- Notes: Candidat 3 (Randria Paul)
INSERT INTO note (id_candidat, id_matiere, id_correcteur, valeur_note) VALUES
    (3, 1, 1, 18),  -- Math, Louis: 18
    (3, 1, 2, 17),  -- Math, Nyaina: 17
    (3, 1, 3, 19),  -- Math, Mikolo: 19
    (3, 2, 1, 5),   -- Physique, Louis: 5
    (3, 2, 2, 15),  -- Physique, Nyaina: 15
    (3, 2, 3, 8),   -- Physique, Mikolo: 8
    (3, 3, 1, 11),  -- Info, Louis: 11
    (3, 3, 2, 12),  -- Info, Nyaina: 12
    (3, 3, 3, 10);  -- Info, Mikolo: 10
