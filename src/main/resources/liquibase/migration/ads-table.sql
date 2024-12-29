-- liquibase formatted sql

-- changeset edkamenskikh:1

CREATE TABLE IF NOT EXISTS ads(
    id SERIAL NOT NULL,
    price INT NOT NULL,
    title VARCHAR(32),
    description VARCHAR(64),
    image VARCHAR(255),
    author_id INT NOT NULL,

    PRIMARY KEY (id),

    FOREIGN KEY (author_id) REFERENCES users(id)
);