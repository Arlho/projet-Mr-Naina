CREATE TABLE correcteur (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(255) NOT NULL
);


CREATE TABLE matiere (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(255) NOT NULL
);

CREATE TABLE candidat (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(255) NOT NULL
);

CREATE TABLE resolution (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(255) NOT NULL
);

CREATE TABLE operateur (
    id SERIAL PRIMARY KEY,
    symbole 
);

CREATE TABLE parametre (
    id SERIAL PRIMARY KEY,
    id matiere VARCHAR(255) NOT NULL,
    diff
    id operateur
    id resolution
);

CREATE TABLE note (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(255) NOT NULL
);