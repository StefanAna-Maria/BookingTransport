CREATE DATABASE IF NOT EXISTS booking_transport
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
USE booking_transport;
DROP TABLE IF EXISTS RAPORT;
DROP TABLE IF EXISTS PLATA;
DROP TABLE IF EXISTS FEEDBACK;
DROP TABLE IF EXISTS CURSA;
DROP TABLE IF EXISTS CERERE_TRANSPORT;
DROP TABLE IF EXISTS VEHICUL;
DROP TABLE IF EXISTS SOFER;
DROP TABLE IF EXISTS CLIENT;
DROP TABLE IF EXISTS UTILIZATOR;
DROP TABLE IF EXISTS TARIF;

CREATE TABLE IF NOT EXISTS UTILIZATOR (
                                          id_utilizator   INT AUTO_INCREMENT PRIMARY KEY,
                                          nume            VARCHAR(100)    NOT NULL,
    email           VARCHAR(100)    NOT NULL,
    telefon         VARCHAR(20),
    username        VARCHAR(50)     NOT NULL UNIQUE,
    parola          VARCHAR(100)    NOT NULL,
    rol             ENUM('CLIENT','SOFER','ADMIN') NOT NULL
    ) ENGINE=InnoDB
    DEFAULT CHARSET=utf8mb4
    COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS CLIENT (
                                      id_client       INT PRIMARY KEY,
                                      CONSTRAINT fk_client_utilizator
                                      FOREIGN KEY (id_client)
    REFERENCES UTILIZATOR(id_utilizator)
    ON DELETE CASCADE
    ON UPDATE CASCADE
    ) ENGINE=InnoDB
    DEFAULT CHARSET=utf8mb4
    COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS SOFER (
                                     id_sofer        INT PRIMARY KEY,
                                     CONSTRAINT fk_sofer_utilizator
                                     FOREIGN KEY (id_sofer)
    REFERENCES UTILIZATOR(id_utilizator)
    ON DELETE CASCADE
    ON UPDATE CASCADE
    ) ENGINE=InnoDB
    DEFAULT CHARSET=utf8mb4
    COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS VEHICUL (
                                       id_vehicul          INT AUTO_INCREMENT PRIMARY KEY,
                                       marca               VARCHAR(50)      NOT NULL,
    model               VARCHAR(50)      NOT NULL,
    nr_inmatriculare    VARCHAR(20)      NOT NULL UNIQUE,
    capacitate          INT              NOT NULL,
    viteza_medie        DOUBLE           NOT NULL,
    id_sofer            INT              NULL,
    CONSTRAINT fk_vehicul_sofer
    FOREIGN KEY (id_sofer)
    REFERENCES SOFER(id_sofer)
    ON DELETE SET NULL
    ON UPDATE CASCADE
    ) ENGINE=InnoDB
    DEFAULT CHARSET=utf8mb4
    COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS CERERE_TRANSPORT (
                                                id_cerere       INT AUTO_INCREMENT PRIMARY KEY,
                                                id_client       INT              NOT NULL,
                                                destinatie      VARCHAR(100)     NOT NULL,
    distanta_km     DOUBLE           NOT NULL,
    data_creare     DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status          ENUM('IN_ASTEPTARE','ACCEPTATA','ANULATA')
    NOT NULL DEFAULT 'IN_ASTEPTARE',
    CONSTRAINT fk_cerere_client
    FOREIGN KEY (id_client)
    REFERENCES CLIENT(id_client)
    ON DELETE CASCADE
    ON UPDATE CASCADE
    ) ENGINE=InnoDB
    DEFAULT CHARSET=utf8mb4
    COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS CURSA (
                                     id_cursa           INT AUTO_INCREMENT PRIMARY KEY,
                                     id_cerere          INT              NOT NULL,
                                     id_sofer           INT              NULL,
                                     id_vehicul         INT              NULL,
                                     data_start         DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                     durata_minute      DOUBLE           NOT NULL,
                                     tarif              DOUBLE           NOT NULL,
                                     data_finalizare    DATETIME         NULL,
                                     finalizata         TINYINT(1)       NOT NULL DEFAULT 0,
    CONSTRAINT fk_cursa_cerere
    FOREIGN KEY (id_cerere)
    REFERENCES CERERE_TRANSPORT(id_cerere)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
    CONSTRAINT fk_cursa_sofer
    FOREIGN KEY (id_sofer)
    REFERENCES SOFER(id_sofer)
    ON DELETE SET NULL
    ON UPDATE CASCADE,
    CONSTRAINT fk_cursa_vehicul
    FOREIGN KEY (id_vehicul)
    REFERENCES VEHICUL(id_vehicul)
    ON DELETE SET NULL
    ON UPDATE CASCADE
    ) ENGINE=InnoDB
    DEFAULT CHARSET=utf8mb4
    COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS FEEDBACK (
                                        id_feedback    INT AUTO_INCREMENT PRIMARY KEY,
                                        id_cursa       INT              NOT NULL,
                                        id_sender      INT              NOT NULL,
                                        id_receiver    INT              NOT NULL,
                                        scor           INT              NOT NULL CHECK (scor BETWEEN 1 AND 5),
    comentariu     TEXT,
    tip            ENUM('CLIENT_CATRE_SOFER','SOFER_CATRE_CLIENT')
    NOT NULL,
    CONSTRAINT fk_feedback_cursa
    FOREIGN KEY (id_cursa)
    REFERENCES CURSA(id_cursa)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
    CONSTRAINT fk_feedback_sender
    FOREIGN KEY (id_sender)
    REFERENCES UTILIZATOR(id_utilizator)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
    CONSTRAINT fk_feedback_receiver
    FOREIGN KEY (id_receiver)
    REFERENCES UTILIZATOR(id_utilizator)
    ON DELETE CASCADE
    ON UPDATE CASCADE
    ) ENGINE=InnoDB
    DEFAULT CHARSET=utf8mb4
    COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS PLATA (
                                     id_plata       INT AUTO_INCREMENT PRIMARY KEY,
                                     id_cursa       INT              NOT NULL,
                                     suma           DOUBLE           NOT NULL,
                                     metoda_plata   VARCHAR(50)      NOT NULL,
    data_plata     DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_plata_cursa
    FOREIGN KEY (id_cursa)
    REFERENCES CURSA(id_cursa)
    ON DELETE CASCADE
    ON UPDATE CASCADE
    ) ENGINE=InnoDB
    DEFAULT CHARSET=utf8mb4
    COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS TARIF (
                                     id_tarif       INT AUTO_INCREMENT PRIMARY KEY,
                                     pret_km         DOUBLE           NOT NULL,
                                     pret_min        DOUBLE           NOT NULL,
                                     data_activare   DATE             NOT NULL
) ENGINE=InnoDB
    DEFAULT CHARSET=utf8mb4
    COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS RAPORT (
                                      id_raport       INT AUTO_INCREMENT PRIMARY KEY,
                                      id_sofer        INT              NOT NULL,
                                      venit_total     DOUBLE           NOT NULL,
                                      data_generare   DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                      CONSTRAINT fk_raport_sofer
                                      FOREIGN KEY (id_sofer)
    REFERENCES SOFER(id_sofer)
    ON DELETE CASCADE
    ON UPDATE CASCADE
    ) ENGINE=InnoDB
    DEFAULT CHARSET=utf8mb4
    COLLATE=utf8mb4_unicode_ci;

ALTER TABLE CURSA ADD COLUMN distanta_km DOUBLE NOT NULL;