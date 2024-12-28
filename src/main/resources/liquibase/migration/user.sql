-- liquibase formatted sql

-- changeset edkamenskikh:1

CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       email VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       first_name VARCHAR(255),
                       last_name VARCHAR(255),
                       phone VARCHAR(255),
                       image VARCHAR(255),
                       role VARCHAR(255)
);