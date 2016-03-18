DROP TABLE roles IF EXISTS;
DROP TABLE users IF EXISTS;

CREATE TABLE users  (
    username VARCHAR(60),
    PRIMARY KEY (username)
);

CREATE TABLE roles  (
    id BIGINT IDENTITY NOT NULL PRIMARY KEY,
    username VARCHAR(60),
    rolename VARCHAR(20),
    UNIQUE (username, rolename),
    FOREIGN KEY (username) REFERENCES users(username)
);
