-- liquibase formatted sql

--changeset eduardkamena:1
CREATE TABLE IF NOT EXISTS images(

    id SERIAL NOT NULL,
    filePath TEXT NOT NULL,
    fileSize BIGINT NOT NULL,
    mediaType TEXT NOT NULL,
    byteData BYTEA NOT NULL,

    PRIMARY KEY (id)
);