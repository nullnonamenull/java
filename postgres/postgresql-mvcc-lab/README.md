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

---

# Environment Setup

Start PostgreSQL:

```bash
docker compose up -d
```

Verify teh container:

```bash
docker compose ps
```

---

# Opening Two PostgreSQL Sessions

The experiment require two independent database sessions.

## Temrinal A

```bash
docker compose exec postgres psql -U admin -d mvcc_lab
```

The terminal represent two independent PostgreSQL connections:

```text
Terminal A                  Terminal B
────────────────────────────────────────
Session A                   Session B
Transaction A               Transaction B
```

---

# Useful Commands

Check current data:

```sql
select * from accounts;
```

Start a transaction:

```sql
begin;
```

Commit:

```sql
commit;
```

Rollback:

```sql
rollback;
```

Check the transaction isolation level:

```sql
show transaction_isolation;
```

Reset the account between experiments:

```sql
update accounts
    set balance = 1000
    where id = 1;
```

Verify:

```sql
select * from accounts;
```

---

# Experiment 1 - Uncommitted Changes

## Goal

Determine whether one PostgreSQL session can observe changes made by another before those changes are commited.

---

## Step 1 - Transaction A

### Temrinal A

Start a transaction:

```sql
begin;
```

Modify the account:

```sql
update accounts
    set balance = 500
    where id = 1;
```

Read the modified value:

```sql
select * from accounts where id = 1;
```

Do **not** commit the transaction yet.

---

## Step 2 - Hypothesis

Before querying the same row from Terminal B, predict which balance will be visible.

```text
Expected balance: 1000

Reason: Transaction A has not been commited yet, so its chnages are not visible to other sessions. Session B still sees the previous value.
```

---

## Step 3 - Session B

### Terminal B

```sql
select * from acocunts where id = 1;
```

### Observed Result

```text
Terminal A balance: 500

Terminal B balance: 1000
```

### Explanation

```text
Transaction A has not been commited yet, so its changes are not visible to other sessions.
```

## Step 4 - COmmit Transaction A

### Terminal A - Commit Transaction A

### Terminal A

```sql
commit;
```

### Terminal B

Execute the query again:

```sql
select * from accounts where id = 1;
```

### Observed Result

```text
Balance after transaction A commits: 500
```

### Conslusion

```text
Changes become visible to other transactions only after they are commited.
```

---

# Experiment 2 - READ COMMITED

## Goal

Observe what happens when another transaction modifies and commits a row between two `select` statements executed by the same transaction.

---

## Reset

```sql
update accounts
    set balance = 1000
    where id = 1;
```

Verify:

```sql
select * from accounts where id = 1;
```

---

## Setp 1 - Transaction A

### Terminal A

```sql
begin;
```

Check the isolation level:

```sql
show transaction_isolation;
```

Perform the first read:

```sql
select balance
from accounts
where id = 1;
```

Record the result:

```text
First Select: 1000
```

Keep Transaction A open.

---

## Step 2 - Transaction B

### Terminal B

```sql
begin;
```

Modify the account:

```sql
update accounts
    set balance = 2000
    where id = 1;
```

Commit the change:

```sql
commit;
```

---

## Step 3 - Hypothesis

Transaction A remains open.

Predict what the second `select` executed by Transaction A will return.

```text
First SELECT: 1000

Transaction B commited balande = 2000

Expected second SELECT: 2000

Reason: Under READ COMMITED each statement takes a new snapshot, so the second SELECT in Transaction A sees the value comitted by Transaction B, even though Transaction A was already open.
```

---

## Step 4 - Second Read

### Terminal A

```sql
select balance
from accounts
where id = 1;
```

### Observed Result

```text
First select: 1000

Second select: 2000
```

### Explanation

```text
Under READ COMMITED each statement takes a new snapshot, so the second SELECT in Transaction A sees the value comitted by Transaction B, even though Transaction A was already open.
```

---

## Cleanup

```sql
commit;
```

---