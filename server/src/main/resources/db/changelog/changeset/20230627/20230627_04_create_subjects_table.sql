-- liquibase formatted sql

-- changeset sdvkit:4
CREATE TABLE subjects (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(50) NOT NULL,

    foreign key (user_id) references users(id)
)