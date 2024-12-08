-- liquibase formatted sql

--changeset gitWestender:Create comment table
CREATE TABLE IF NOT EXISTS t_comment
(
    id            SERIAL      NOT NULL,
    c_text        VARCHAR(32) NOT NULL,
    c_date        TIMESTAMP,
    c_author_id   INT         NOT NULL,
    c_announce_id INT         NOT NULL,

    CONSTRAINT comment_pk PRIMARY KEY (id),

    CONSTRAINT fk_author_id FOREIGN KEY (c_author_id) REFERENCES t_user (id),
    CONSTRAINT fk_announce_id FOREIGN KEY (c_announce_id) REFERENCES t_announce (id)
)