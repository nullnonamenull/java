CREATE TABLE accounts (
	id BIGSERIAL PRIMARY KEY,
	owner VARCHAR(255) NOT NULL,
	balance NUMERIC(15, 2) NOT NULL
);

INSERT INTO accounts(owner, balance)
VALUES ('Pioter', 1000.00);