-- Script de migration pour la version 2.0 de l'application "Reservation de salles"
-- Cree les tables si elles n'existent pas puis applique les evolutions

CREATE TABLE IF NOT EXISTS utilisateurs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    prenom VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    telephone VARCHAR(255),
    departement VARCHAR(100),
    version BIGINT
);

CREATE TABLE IF NOT EXISTS salles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    capacite INT NOT NULL,
    description VARCHAR(500),
    batiment VARCHAR(255),
    etage INT,
    numero VARCHAR(255),
    version BIGINT
);

CREATE TABLE IF NOT EXISTS equipements (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    description VARCHAR(500),
    reference VARCHAR(255),
    version BIGINT
);

CREATE TABLE IF NOT EXISTS reservations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    date_debut DATETIME NOT NULL,
    date_fin DATETIME NOT NULL,
    motif VARCHAR(500),
    statut VARCHAR(20) DEFAULT 'CONFIRMEE',
    utilisateur_id BIGINT NOT NULL,
    salle_id BIGINT NOT NULL,
    version BIGINT,
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateurs(id),
    FOREIGN KEY (salle_id) REFERENCES salles(id)
);

CREATE TABLE IF NOT EXISTS salle_equipement (
    salle_id BIGINT NOT NULL,
    equipement_id BIGINT NOT NULL,
    PRIMARY KEY (salle_id, equipement_id),
    FOREIGN KEY (salle_id) REFERENCES salles(id),
    FOREIGN KEY (equipement_id) REFERENCES equipements(id)
);

CREATE INDEX idx_reservation_dates ON reservations(date_debut, date_fin);
CREATE INDEX idx_reservation_statut ON reservations(statut);
CREATE INDEX idx_salle_capacite ON salles(capacite);
CREATE INDEX idx_salle_batiment ON salles(batiment);

CREATE TABLE IF NOT EXISTS db_version (
    id INT PRIMARY KEY,
    version VARCHAR(10),
    date_mise_a_jour TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO db_version (id, version) VALUES (1, '2.0')
    ON DUPLICATE KEY UPDATE version = '2.0', date_mise_a_jour = CURRENT_TIMESTAMP;
