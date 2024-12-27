-- liquibase formatted sql

--changeset eduardkamena:1
CREATE TABLE IF NOT EXISTS images(

    id SERIAL NOT NULL,
    file_path TEXT NOT NULL,
    file_size BIGINT NOT NULL,
    media_type TEXT NOT NULL,
    byte_data BIGINT NOT NULL,

    PRIMARY KEY (id)
);