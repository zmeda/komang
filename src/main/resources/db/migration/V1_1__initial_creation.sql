CREATE TABLE IF NOT EXISTS PUBLIC."application" ("application_id" VARCHAR NOT NULL PRIMARY KEY, "name" VARCHAR NOT NULL);

CREATE TABLE IF NOT EXISTS PUBLIC."profile" ("profile_id" VARCHAR NOT NULL PRIMARY KEY, "application_id" VARCHAR NOT NULL, "name" VARCHAR NOT NULL);

ALTER TABLE PUBLIC."profile" ADD FOREIGN KEY ("application_id") REFERENCES PUBLIC."application" ("application_id");
