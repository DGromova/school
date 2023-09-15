-- liquibase formatted sql

-- changeset daria:1
-- preconditions onFail:MARK_RAN
-- precondition-sql-check expectedResult:0 select count(*) from pg_catalog.pg_tables t inner join pg_indexes i on i.tablename = t.tablename where t.tablename = 'student' and i.indexname = 'student_name_index';
-- rollback DROP INDEX student_name_index
CREATE INDEX student_name_index ON student (name);

-- changeset daria:2
-- preconditions onFail:MARK_RAN
-- precondition-sql-check expectedResult:0 select count(*) from pg_catalog.pg_tables t inner join pg_indexes i on i.tablename = t.tablename where t.tablename = 'faculty' and i.indexname = 'faculty_sh_idx';
-- rollback DROP INDEX faculty_sh_idx
CREATE UNIQUE INDEX faculty_sh_idx ON faculty (name, color);