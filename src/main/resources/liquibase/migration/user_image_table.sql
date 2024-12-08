-- liquibase formatted sql

--changeset gitWestender:Create User image table
CREATE TABLE IF NOT EXISTS t_user_image
(
    id SERIAL NOT NULL,
    c_image TEXT,

    CONSTRAINT user_image_pk PRIMARY KEY (id)
);