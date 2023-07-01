-- liquibase formatted sql

-- changeset sdvkit:6
CREATE TABLE second_lvl_statements (
    id BIGSERIAL PRIMARY KEY,
    subject_id BIGINT NOT NULL,
    first_lvl_statement_id BIGINT NOT NULL,

    foreign key (subject_id) references subjects(id),
    foreign key (first_lvl_statement_id) references first_lvl_statements(id)
)