-- liquibase formatted sql

-- changeset sdvkit:2
CREATE TABLE groups (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(15) NOT NULL,
    created_at DATE NOT NULL,
    is_archived BOOLEAN NOT NULL,

    foreign key (user_id) references users(id)
)