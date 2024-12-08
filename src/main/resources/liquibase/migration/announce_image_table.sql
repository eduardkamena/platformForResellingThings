-- liquibase formatted sql

--changeset gitWestender:Create Announce image table
CREATE TABLE IF NOT EXISTS t_announce_image
(
    id SERIAL NOT NULL,
    c_image TEXT,

    CONSTRAINT announce_image_pk PRIMARY KEY (id)
);