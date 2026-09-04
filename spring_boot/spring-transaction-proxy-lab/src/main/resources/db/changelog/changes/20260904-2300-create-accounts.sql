--liquibase formatted sql

--changeset noname:20260904-2300-create-accounts
create table accounts (
    id uuid primary key default gen_random_uuid(),
    amount numeric(19, 2) not null default 0
);
--rollback drop table accounts;