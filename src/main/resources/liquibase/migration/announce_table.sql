-- liquibase formatted sql

--changeset gitWestender:Create announce table
CREATE TABLE IF NOT EXISTS t_announce
(
    id               SERIAL NOT NULL,
    c_author_id      BIGINT NOT NULL,
    c_description    TEXT,
    c_announce_image TEXT,
    c_price          BIGINT NOT NULL,
    c_title          VARCHAR(64),

    CONSTRAINT fk_author_id FOREIGN KEY (c_author_id) REFERENCES t_user(id),
    CONSTRAINT announce_pk PRIMARY KEY (id)
);