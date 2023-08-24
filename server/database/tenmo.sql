BEGIN TRANSACTION;

DROP TABLE IF EXISTS tenmo_user, account, status, transfer;

DROP SEQUENCE IF EXISTS seq_user_id, seq_account_id, seq_transfer_id;

-- Sequence to start user_id values at 1001 instead of 1
CREATE SEQUENCE seq_user_id
  INCREMENT BY 1
  START WITH 1001
  NO MAXVALUE;

CREATE TABLE tenmo_user (
	user_id int NOT NULL DEFAULT nextval('seq_user_id'),
	username varchar(50) NOT NULL,
	password_hash varchar(200) NOT NULL,
	CONSTRAINT PK_tenmo_user PRIMARY KEY (user_id),
	CONSTRAINT UQ_username UNIQUE (username)
);

-- Sequence to start account_id values at 2001 instead of 1
-- Note: Use similar sequences with unique starting values for additional tables
CREATE SEQUENCE seq_account_id
  INCREMENT BY 1
  START WITH 2001
  NO MAXVALUE;

CREATE TABLE account (
	account_id int NOT NULL DEFAULT nextval('seq_account_id'),
	user_id int NOT NULL,
	balance numeric(13, 2) NOT NULL,
	CONSTRAINT PK_account PRIMARY KEY (account_id),
	CONSTRAINT FK_account_tenmo_user FOREIGN KEY (user_id) REFERENCES tenmo_user (user_id)
);

CREATE TABLE status (
	status_id int NOT NULL,
	status_name varchar NOT NULL,
	CONSTRAINT PK_status PRIMARY KEY (status_id)
);

CREATE SEQUENCE seq_transfer_id
  INCREMENT BY 1
  START WITH 3001
  NO MAXVALUE;

CREATE TABLE transfer (
	transfer_id int NOT NULL DEFAULT nextval('seq_transfer_id'),
	transfer_amount numeric(13, 2) NOT NULL,
	from_user_id int NOT NULL,
	to_user_id int NOT NULL,
	date DATE NOT NULL DEFAULT current_date,
	status int NOT NULL,
	CONSTRAINT PK_transfer PRIMARY KEY (transfer_id),
	CONSTRAINT FK_transfer_from FOREIGN KEY (from_user_id) REFERENCES tenmo_user (user_id),
	CONSTRAINT FK_transfer_to FOREIGN KEY (to_user_id) REFERENCES tenmo_user (user_id),
	CONSTRAINT FK_status FOREIGN KEY (status) REFERENCES status (status_id)
);

INSERT INTO status (status_id, status_name) VALUES (1,'Approved');
INSERT INTO status (status_id, status_name) VALUES (2,'Pending');
INSERT INTO status (status_id, status_name) VALUES (3,'Rejected');

COMMIT;