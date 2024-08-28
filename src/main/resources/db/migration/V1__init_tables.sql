CREATE TABLE IF NOT EXISTS currencies (
    id        SERIAL     PRIMARY KEY,
    code      VARCHAR(3) NOT NULL UNIQUE,
    full_name VARCHAR    NOT NULL,
    sign      VARCHAR    NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS exchange_rates (
    id                 SERIAL         PRIMARY KEY,
    base_currency_id   INT            NOT NULL,
    target_currency_id INT            NOT NULL,
    rate               DECIMAL(16, 6) NOT NULL,
    FOREIGN KEY (base_currency_id)   REFERENCES currencies(id),
    FOREIGN KEY (target_currency_id) REFERENCES currencies(id),
    CONSTRAINT exchange_rates_unique_currency_ids UNIQUE (base_currency_id, target_currency_id),
    CONSTRAINT check_different_currency_ids       CHECK (base_currency_id <> target_currency_id)
);