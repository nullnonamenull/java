# PostgreSQL MVCC Lab

A particular laboratory demonstrating how PostgreSQL handles concurrent transactions using **MVCC (Multi-Version Concurrency Control)** and how transaction isolation levels affect data visibility.

The project uses two independent PostgreSQL sessions to demonstrate transaction behavior directly, without abstractoins introduced by application frameworks or ORMs.

## Learning Objectives

This laboratory demonstrates:

- database transaction boundaries,
- visibility of committed and uncommited changes,
- Multi-Version Concurrency Control (MVCC),
- transaction snapshots,
- PostgreSQL's default `READ COMMITED` isolation level,
- `REPEATABLE READ` isolation,
- differences between `READ COMMITED` and `REPEATABLE READ`,
- concurrent access to the same data.

---

## Project Structure

```text
postgresql-mvcc-lab/
├── docker-compose.yml
├── init.sql
└── README.md
```

The project intentionally does not use:

- Java,
- Spring Boot,
- Hibernate/JPA,
- Liquibase

All experiments are executed directly against PostgreSQL to expose the underlying database behavior.

---

# Database

The experiments use a simple `accounts` table:

```sql
create table accounts (
    id bigserial primary key,
    owner varchar(100) not null,
    balance numeric(15,2) not null
);
```

Initial state:

```text
id      = 1
owner   = Pioter
balance = 1000.00
```