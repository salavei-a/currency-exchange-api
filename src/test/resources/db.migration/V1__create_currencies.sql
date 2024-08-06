CREATE TABLE IF NOT EXISTS currencies (
    id        SERIAL     PRIMARY KEY,
    code      VARCHAR    NOT NULL UNIQUE,
    full_name VARCHAR    NOT NULL,
    sign      VARCHAR    NOT NULL UNIQUE
);