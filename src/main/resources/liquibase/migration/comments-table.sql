-- liquibase formatted sql

-- changeset edkamenskikh:1

CREATE TABLE IF NOT EXISTS comments(
    id SERIAL NOT NULL,
    created_at BIGINT,
    text VARCHAR(32) NOT NULL,
    author_id INT NOT NULL,
    ad_id INT NOT NULL,

    PRIMARY KEY (id),

    FOREIGN KEY (author_id) REFERENCES  users(id),
    FOREIGN KEY (ad_id) REFERENCES ads(id)
);