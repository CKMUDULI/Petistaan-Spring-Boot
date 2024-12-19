DROP DATABASE IF EXISTS petistaan;
CREATE DATABASE petistaan;
USE petistaan;

CREATE TABLE owner_table (
	id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
	city VARCHAR(255) NOT NULL,
	email_id VARCHAR(255) NOT NULL UNIQUE,
	first_name VARCHAR(255) NOT NULL,
	gender ENUM ('F', 'M') NOT NULL,
	last_name VARCHAR(255) NOT NULL,
	mobile_number VARCHAR(10) NOT NULL UNIQUE,
	state VARCHAR(255) NOT NULL
);

CREATE TABLE pet_table (
	id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
	gender ENUM ('F', 'M') NOT NULL,
	name VARCHAR(255) NOT NULL,
	type ENUM ('BIRD', 'CAT', 'DOG', 'FISH', 'RABBIT') NOT NULL
);

CREATE TABLE domestic_pet_table (
	date_of_birth DATE NOT NULL,
	id INTEGER NOT NULL PRIMARY KEY
);

CREATE TABLE wild_pet_table (
	place_of_birth VARCHAR(255) NOT NULL,
	id INTEGER NOT NULL PRIMARY KEY
);

CREATE TABLE owner_pet_table (
	owner_id INTEGER NOT NULL,
	pet_id INTEGER NOT NULL,
	PRIMARY KEY (owner_id, pet_id)
);

ALTER TABLE domestic_pet_table
	ADD CONSTRAINT FK_domestic_pet FOREIGN KEY (id) REFERENCES pet_table (id);

ALTER TABLE wild_pet_table
	ADD CONSTRAINT FK_wild_pet FOREIGN KEY (id) REFERENCES pet_table (id);

ALTER TABLE owner_pet_table
	ADD CONSTRAINT FK_owner_pet_pet FOREIGN KEY (pet_id) REFERENCES pet_table (id),
	ADD CONSTRAINT FK_owner_pet_owner FOREIGN KEY (owner_id) REFERENCES owner_table (id);