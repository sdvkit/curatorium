-- liquibase formatted sql

-- changeset sdvkit:7
CREATE TABLE marks (
    id BIGSERIAL PRIMARY KEY,
    second_lvl_statement_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    value INT NOT NULL,
    mark_type INT,

    foreign key (second_lvl_statement_id) references second_lvl_statements(id),
    foreign key (student_id) references students(id)
)