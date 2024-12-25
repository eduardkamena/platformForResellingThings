-- liquibase formatted sql

--changeset eduardkamena:1
CREATE TABLE IF NOT EXISTS users(

    id SERIAL NOT NULL,
    username VARCHAR(32) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    firstName VARCHAR(16) NOT NULL,
    lastName VARCHAR(16) NOT NULL,
    phone VARCHAR(18) UNIQUE NOT NULL,
    role VARCHAR(32) NOT NULL,
    image INT,

    PRIMARY KEY (id),

    FOREIGN KEY (image) REFERENCES images(id)
);