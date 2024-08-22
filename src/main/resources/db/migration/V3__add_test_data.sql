INSERT INTO currencies (full_name, code, sign)
VALUES ('US Dollar', 'USD', '$'),
       ('Euro', 'EUR', '€'),
       ('Russian Ruble', 'RUB', '₽'),
       ('Georgian lari', 'GEL', '₾'),
       ('Belarussian Ruble', 'BYN', 'Br');

DO $$
DECLARE
    usd_id INT;
    eur_id INT;
    rub_id INT;
    gel_id INT;
    byn_id INT;
BEGIN
    SELECT id INTO usd_id FROM currencies WHERE code = 'USD';
    SELECT id INTO eur_id FROM currencies WHERE code = 'EUR';
    SELECT id INTO rub_id FROM currencies WHERE code = 'RUB';
    SELECT id INTO gel_id FROM currencies WHERE code = 'GEL';
    SELECT id INTO byn_id FROM currencies WHERE code = 'BYN';

    INSERT INTO exchange_rates (base_currency_id, target_currency_id, rate)
    VALUES (usd_id, eur_id, 0.899553),
           (usd_id, rub_id, 91.078844),
           (usd_id, gel_id, 2.701924),
           (usd_id, byn_id, 3.270446),
           (eur_id, rub_id, 101.13126),
           (eur_id, gel_id, 3.003484);
END $$;