-- liquibase formatted sql

-- changeset edkamenskikh:1

CREATE TABLE IF NOT EXISTS users(
    id SERIAL NOT NULL,
    email VARCHAR(32) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(16) NOT NULL,
    last_name VARCHAR(16) NOT NULL,
    phone VARCHAR(18) UNIQUE NOT NULL,
    image VARCHAR(255),
    role VARCHAR(32) NOT NULL,

    PRIMARY KEY (id)
);