-- liquibase formatted sql

-- changeset sdvkit:5
CREATE TABLE first_lvl_statements (
    id BIGSERIAL PRIMARY KEY,
    group_id BIGINT NOT NULL,
    name VARCHAR(50) NOT NULL,

    foreign key (group_id) references groups(id)
)