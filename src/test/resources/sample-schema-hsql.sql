DROP TABLE users IF EXISTS;
CREATE TABLE users (
    id         INT IDENTITY PRIMARY KEY,
    first_name VARCHAR(32) NOT NULL,
    last_name  VARCHAR(32) NOT NULL,
    age        INT         NOT NULL
);