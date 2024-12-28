-- liquibase formatted sql

--changeset eduardkamena:1
CREATE TABLE IF NOT EXISTS ads(

    id SERIAL NOT NULL,
    title VARCHAR(32),
    price INT NOT NULL,
    description VARCHAR(64),
    author_id INT NOT NULL,
    image_id INT,

    PRIMARY KEY (id),

    FOREIGN KEY (author_id) REFERENCES users(id),
    FOREIGN KEY (image_id) REFERENCES images(id)
);