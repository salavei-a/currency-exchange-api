CREATE TABLE IF NOT EXISTS exchange_rates (
    id                 SERIAL         PRIMARY KEY,
    base_currency_id   INT            NOT NULL,
    target_currency_id INT            NOT NULL,
    rate               DECIMAL(16, 6) NOT NULL,
    FOREIGN KEY (base_currency_id)   REFERENCES currencies(id),
    FOREIGN KEY (target_currency_id) REFERENCES currencies(id),
    UNIQUE (base_currency_id, target_currency_id)
);