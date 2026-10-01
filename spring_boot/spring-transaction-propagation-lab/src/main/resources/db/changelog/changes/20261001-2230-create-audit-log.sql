--liquibase formatted sql

--changeset noname:20261001-2230-create-audit-log
create table audit_logs (
                        id uuid primary key default gen_random_uuid(),
                        message varchar(500)
)
--rollback drop table accounts;