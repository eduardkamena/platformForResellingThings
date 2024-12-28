-- liquibase formatted sql

-- changeset edkamenskikh:1

CREATE TABLE comments
(
    id         SERIAL PRIMARY KEY,
    created_at TIMESTAMP,
    text       TEXT,
    user_id    INTEGER REFERENCES users (id)
);

-- changeset edkamenskikh:2

ALTER TABLE comments
    ADD COLUMN ads_id INTEGER REFERENCES ads (id);