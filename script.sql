CREATE TABLE client (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    contact VARCHAR(100)
);

CREATE TABLE demande (
    id SERIAL PRIMARY KEY,
    date DATE NOT NULL,
    idclient INT NOT NULL REFERENCES client(id),
    lieu VARCHAR(100),
    districk VARCHAR(100)
);
