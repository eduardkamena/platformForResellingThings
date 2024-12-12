-- liquibase formatted sql

--changeset gitWestender:Create user table
CREATE TABLE IF NOT EXISTS t_user
(
    id          SERIAL             NOT NULL,
    c_username  VARCHAR(32) UNIQUE NOT NULL,
    c_password  VARCHAR(255)        NOT NULL,
    c_firstname VARCHAR(16)        NOT NULL,
    c_lastname  VARCHAR(16)        NOT NULL,
    c_phone     VARCHAR(18) UNIQUE NOT NULL,
    c_role      VARCHAR(32)        NOT NULL,
    c_image     INT,

    CONSTRAINT user_pk PRIMARY KEY (id),

    CONSTRAINT fk_user_image_id FOREIGN KEY (c_image) REFERENCES t_user_image (id)
);