-- liquibase formatted sql

--changeset eduardkamena:1
CREATE TABLE IF NOT EXISTS images(

    id SERIAL NOT NULL,
    file_path TEXT,
    file_size BIGINT,
    media_type TEXT,
    byte_data BIGINT,

    PRIMARY KEY (id)
);