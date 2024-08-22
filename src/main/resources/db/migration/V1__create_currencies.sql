CREATE TABLE IF NOT EXISTS currencies (
    id        SERIAL     PRIMARY KEY,
    code      VARCHAR(3) NOT NULL UNIQUE,
    full_name VARCHAR    NOT NULL,
    sign      VARCHAR    NOT NULL UNIQUE
);