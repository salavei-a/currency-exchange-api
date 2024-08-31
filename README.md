# Currency Exchange REST API

## Overview

This project provides a REST API for managing currencies and exchange rates. It allows users to view, edit currency
lists and exchange rates, and perform currency exchange.

## Technologies

- **Java Servlets**
- **JDBC**
- **Maven**
- **PostgreSQL**
- **Flyway**
- **Docker / Docker Compose**
- **Nginx**

## Database Schema

### Currencies Table

| Column    | Type       | Description                                  |
|-----------|------------|----------------------------------------------|
| id        | SERIAL     | Primary key, auto-incremented                |
| code      | VARCHAR(3) | Currency code, must be unique and not null   |
| full_name | VARCHAR    | Full name of the currency, not null          |
| sign      | VARCHAR    | Currency symbol, must be unique and not null |

### Exchange Rates Table

| Column             | Type           | Description                                          |
|--------------------|----------------|------------------------------------------------------|
| id                 | SERIAL         | Primary key, auto-incremented                        |
| base_currency_id   | INT            | Foreign key referencing `currencies(id)`, not null   |
| target_currency_id | INT            | Foreign key referencing `currencies(id)`, not null   |
| rate               | DECIMAL(16, 6) | Exchange rate from base to target currency, not null |

**Constraints:**

- Unique constraint on `base_currency_id` and `target_currency_id`

## API Endpoints

**Note**: The currency codes follow the [ISO 4217 standard](https://www.iban.com/currency-codes).

### Currencies

- **GET api/currencies**
    - Retrieves a list of all available currencies

```json
[
  {
    "id": 1,
    "name": "US Dollar",
    "code": "USD",
    "sign": "$"
  },
  {
    "id": 2,
    "name": "Euro",
    "code": "EUR",
    "sign": "€"
  },
  "..."
]
```

- **GET api/currency/USD**
    - Retrieves details of a specific currency by its code

```json
{
  "id": 1,
  "name": "US Dollar",
  "code": "USD",
  "sign": "$"
}
```

- **POST api/currencies**
    - Adds a new currency to the database. The data should be sent as `x-www-form-urlencoded`. The following form fields
      are required:
        - **name**: The full name of the currency.
        - **code**: The 3-letter currency code (e.g., USD, EUR).
        - **sign**: The symbol of the currency (e.g., $, €).

```json
{
  "id": 6,
  "name": "Pound Sterling",
  "code": "GBP",
  "sign": "£"
}
```

### Exchange Rates

- **GET api/exchangeRate/USDGEL**
    - Retrieves a specific exchange rate between two currencies

```json
{
  "id": 3,
  "baseCurrency": {
    "id": 1,
    "name": "US Dollar",
    "code": "USD",
    "sign": "$"
  },
  "targetCurrency": {
    "id": 4,
    "name": "Georgian lari",
    "code": "GEL",
    "sign": "₾"
  },
  "rate": 2.701924
}
```

- **GET api/exchangeRates**
    - Retrieves a list of all exchange rates

```json
[
  {
    "id": 1,
    "baseCurrency": {
      "id": 1,
      "name": "US Dollar",
      "code": "USD",
      "sign": "$"
    },
    "targetCurrency": {
      "id": 2,
      "name": "Euro",
      "code": "EUR",
      "sign": "€"
    },
    "rate": 0.899553
  },
  {
    "id": 2,
    "baseCurrency": {
      "id": 1,
      "name": "US Dollar",
      "code": "USD",
      "sign": "$"
    },
    "targetCurrency": {
      "id": 3,
      "name": "Russian Ruble",
      "code": "RUB",
      "sign": "₽"
    },
    "rate": 91.078844
  },
  "..."
]
```

- **POST api/exchangeRates**
    - Adds a new exchange rate. The data should be sent as `x-www-form-urlencoded`. The following form fields are
      required:
        - **baseCurrencyCode**: The 3-letter code of the base currency
        - **targetCurrencyCode**: The 3-letter code of the target currency
        - **rate**: The exchange rate

```json
{
  "id": 7,
  "baseCurrency": {
    "id": 4,
    "name": "Georgian lari",
    "code": "GEL",
    "sign": "₾"
  },
  "targetCurrency": {
    "id": 5,
    "name": "Belarussian Ruble",
    "code": "BYN",
    "sign": "Br"
  },
  "rate": 1.212499
}
```

- **PATCH api/exchangeRate/USDEUR**
    - Updates an existing exchange rate. The **rate** should be sent as `x-www-form-urlencoded`

```json
{
  "id": 1,
  "baseCurrency": {
    "id": 1,
    "name": "US Dollar",
    "code": "USD",
    "sign": "$"
  },
  "targetCurrency": {
    "id": 2,
    "name": "Euro",
    "code": "EUR",
    "sign": "€"
  },
  "rate": 0.904151
}
```

### Currency Exchange

- **GET api/exchange?from=USD&to=EUR&amount=100**
    - Automatically performs currency conversion based on the specified amount and currency codes. The conversion
      process will select the most appropriate method:
        - **Direct Exchange Rate**: Converts directly between the base and target currencies if a direct exchange rate
          is available
        - **Reverse Exchange Rate**: Uses the reverse of the exchange rate if the direct rate is not available
        - **Cross Exchange Rate via USD**: If neither a direct nor reverse rate is available, the conversion is done via
          a cross exchange rate using USD as an intermediary currency

```json
{
  "baseCurrency": {
    "id": 1,
    "name": "US Dollar",
    "code": "USD",
    "sign": "$"
  },
  "targetCurrency": {
    "id": 2,
    "name": "Euro",
    "code": "EUR",
    "sign": "€"
  },
  "rate": 0.904151,
  "amount": 100,
  "convertedAmount": 90.42
}
```

## HTTP Requests ##

You can find example HTTP requests in the [http-requests](./http-requests) directory

## Local project startup

To start the project locally, use the provided [docker-compose.yml](docker-compose.yml) file. This setup is intended for
development and testing purposes on a local machine.

**Key Points:**

- **Docker Containers:** The local startup configuration includes Docker containers for a test frontend located in
  the [`frontend`](./frontend) directory and PostgreSQL.
- **Tomcat:** You will need to install and configure Tomcat manually or run it using Intellij Idea

**Steps to Run the Project Locally:**

1. Ensure you have Docker and Docker Compose installed locally/on server.
2. Open your terminal in the project's root directory and run the following command to start the Docker containers in
   detached mode:
   ```bash
   docker-compose up -d
3. Run Tomcat using Intellij Idea

You can also use Docker Desktop for convenience.

## Deployment

**Key Points:**

- **Docker Containers:** The server deployment configuration includes Docker containers for PostgreSQL, Tomcat and
  Nginx.

For server deployment, use the [docker-compose.prod.yml](docker-compose.prod.yml) file.

You need to update the IP address in the frontend configuration file located
at `frontend/js/app.js`. Change the line:

```javascript
// const host = "http://server-ip:8888/api"
```

to

```javascript
const host = "http://your-server-ip:port/api";
```

**Important Note:**

If you use my [forward Nginx configuration](forward) then the port will be 8888; otherwise, it will be 8181
as specified in the docker-compose configuration.

**Steps to deploy on a server:**

1. Ensure you have Docker and Docker Compose installed on your server.
2. Copy `docker-compose.prod.yml`, `.env` and `frontend` folder to your server.
3. Navigate to the directory where the docker-compose.prod.yml file is located.
4. Run the following command to start the Docker containers in detached mode:

   ```bash
   docker-compose -f docker-compose.prod.yml up -d
   ```

## Contributing

Contributions are welcome! Please fork this repository and submit a pull request

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.


