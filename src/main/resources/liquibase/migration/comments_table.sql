-- liquibase formatted sql

--changeset eduardkamena:1
CREATE TABLE IF NOT EXISTS comments(

    pk SERIAL NOT NULL,
    text VARCHAR(32) NOT NULL,
    date_time BIGINT,
    author_id INT NOT NULL,
    ad_id INT NOT NULL,

    PRIMARY KEY (pk),

    FOREIGN KEY (author_id) REFERENCES users(id),
    FOREIGN KEY (ad_id) REFERENCES ads(pk)
);