--liquibase formatted sql

--changeset noname:20260930-2126-create-orders
create table orders (
    id uuid primary key default gen_random_uuid()
)
--rollback drop table accounts;