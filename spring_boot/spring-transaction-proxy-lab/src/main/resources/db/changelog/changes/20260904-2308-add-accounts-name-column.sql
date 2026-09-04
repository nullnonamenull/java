--liquibase formatted SQL

--changeset noname:20260904-2308-add-accounts-name-column
alter table accounts add column name varchar(100) not null default '';

--rollback alter table accounts drop column name;