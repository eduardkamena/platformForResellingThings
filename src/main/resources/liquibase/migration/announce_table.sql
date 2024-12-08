-- liquibase formatted sql

--changeset gitWestender:Create announce table
CREATE TABLE IF NOT EXISTS t_announce
(
    id               SERIAL NOT NULL,
    c_author_id      INT    NOT NULL,
    c_description    VARCHAR(64),
    c_announce_image INT,
    c_price          INT    NOT NULL,
    c_title          VARCHAR(32),

    CONSTRAINT announce_pk PRIMARY KEY (id),

    CONSTRAINT fk_author_id FOREIGN KEY (c_author_id) REFERENCES t_user (id),
    CONSTRAINT fk_announce_image_id FOREIGN KEY (c_announce_image) REFERENCES t_announce_image (id)
);