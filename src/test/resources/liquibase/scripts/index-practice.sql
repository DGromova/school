-- liquibase formatted sql

-- changeset daria:1
CREATE INDEX student_name_index ON student (name);

-- changeset daria:1
CREATE UNIQUE INDEX faculty_sh_idx ON faculty (name, color);