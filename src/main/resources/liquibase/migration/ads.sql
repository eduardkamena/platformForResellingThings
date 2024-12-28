-- liquibase formatted sql

-- changeset edkamenskikh:1

CREATE TABLE ads
(
    id          SERIAL PRIMARY KEY,
    price       INTEGER,
    title       VARCHAR(255),
    description TEXT,
    image       VARCHAR(255),
    user_id     INTEGER REFERENCES users (id)
);