-- liquibase formatted sql

-- changeset edkamenskikh:1

CREATE TABLE IF NOT EXISTS images(
    id SERIAL NOT NULL,
    file_path TEXT NOT NULL,
    file_size BIGINT NOT NULL,
    media_type TEXT NOT NULL,
    data BIGINT NOT NULL,

    PRIMARY KEY (id)
);