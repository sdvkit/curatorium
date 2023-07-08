-- liquibase formatted sql

-- changeset sdvkit:1
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(25) NOT NULL,
    username VARCHAR(15) NOT NULL UNIQUE,
    password VARCHAR(256) NOT NULL,
    role VARCHAR(50) NOT NULL,
    is_logged_in BOOLEAN NOT NULL
)