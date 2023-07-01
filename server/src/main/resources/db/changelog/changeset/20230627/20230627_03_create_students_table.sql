-- liquibase formatted sql

-- changeset sdvkit:3
CREATE TABLE students (
    id BIGSERIAL PRIMARY KEY,
    group_id BIGINT NOT NULL,
    name VARCHAR(50) NOT NULL,

    foreign key (group_id) references groups(id)
)