package com.asalavei.currencyexchange.api.dbaccess.repositories;

import com.asalavei.currencyexchange.api.dbaccess.entities.EntityCurrency;
import com.asalavei.currencyexchange.api.dbaccess.entities.EntityExchangeRate;
import com.asalavei.currencyexchange.api.dbaccess.ConnectionManager;
import com.asalavei.currencyexchange.api.exceptions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class JdbcExchangeRateDao extends BaseJdbcDao<EntityExchangeRate> implements ExchangeRateRepository {

    private static final String EXCHANGE_RATE = "%s/%s exchange rate";

    @Override
    public EntityExchangeRate save(EntityExchangeRate entity) {
        String query = "WITH inserted AS (" +
                       "INSERT INTO exchange_rates (base_currency_id, target_currency_id, rate) " +
                       "SELECT bc.id, tc.id, ? " +
                       "FROM currencies bc " +
                       "JOIN currencies tc ON tc.code = ? " +
                       "WHERE bc.code = ? " +
                       "RETURNING id, base_currency_id, target_currency_id, rate) " +
                       "SELECT i.id AS exchange_rate_id, i.rate AS rate, " +
                       "bc.id AS base_currency_id, bc.full_name AS base_currency_name, bc.code AS base_currency_code, bc.sign AS base_currency_sign, " +
                       "tc.id AS target_currency_id, tc.full_name AS target_currency_name, tc.code AS target_currency_code, tc.sign AS target_currency_sign " +
                       "FROM inserted i " +
                       "JOIN currencies bc ON i.base_currency_id = bc.id " +
                       "JOIN currencies tc ON i.target_currency_id = tc.id";

        return save(query, entity.getRate(), entity.getTargetCurrency().getCode(), entity.getBaseCurrency().getCode());
    }

    @Override
    public Collection<EntityExchangeRate> findAll() {
        String query = "SELECT er.id AS exchange_rate_id, er.rate AS rate, " +
                       "bc.id AS base_currency_id, bc.full_name AS base_currency_name, bc.code AS base_currency_code, bc.sign AS base_currency_sign, " +
                       "tc.id AS target_currency_id, tc.full_name AS target_currency_name, tc.code AS target_currency_code, tc.sign AS target_currency_sign " +
                       "FROM exchange_rates er " +
                       "JOIN currencies bc ON er.base_currency_id = bc.id " +
                       "JOIN currencies tc ON er.target_currency_id = tc.id " +
                       "ORDER BY er.id";

        return findAll(query);

    }

    @Override
    public Optional<EntityExchangeRate> findByCurrencyCodes(String baseCurrencyCode, String targetCurrencyCode) {
        String query = "SELECT er.id AS exchange_rate_id, er.rate AS rate, " +
                       "bc.id AS base_currency_id, bc.full_name AS base_currency_name, bc.code AS base_currency_code, bc.sign AS base_currency_sign, " +
                       "tc.id AS target_currency_id, tc.full_name AS target_currency_name, tc.code AS target_currency_code, tc.sign AS target_currency_sign " +
                       "FROM exchange_rates er " +
                       "JOIN currencies bc ON er.base_currency_id = bc.id " +
                       "JOIN currencies tc ON er.target_currency_id = tc.id " +
                       "WHERE (bc.code, tc.code) = (?, ?)";

        return findByCodes(query, baseCurrencyCode, targetCurrencyCode);
    }

    @Override
    public EntityExchangeRate update(EntityExchangeRate entity) {
        String baseCurrencyCode = entity.getBaseCurrency().getCode();
        String targetCurrencyCode = entity.getTargetCurrency().getCode();

        String query = "UPDATE exchange_rates er " +
                       "SET rate = ? " +
                       "FROM currencies bc, currencies tc " +
                       "WHERE (er.base_currency_id, er.target_currency_id) = (bc.id, tc.id) " +
                       "AND (bc.code, tc.code) = (?, ?) " +
                       "RETURNING er.id AS exchange_rate_id, er.rate AS rate, " +
                       "bc.id AS base_currency_id, bc.full_name AS base_currency_name, bc.code AS base_currency_code, bc.sign AS base_currency_sign, " +
                       "tc.id AS target_currency_id, tc.full_name AS target_currency_name, tc.code AS target_currency_code, tc.sign AS target_currency_sign";

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setBigDecimal(1, entity.getRate());
            preparedStatement.setString(2, baseCurrencyCode);
            preparedStatement.setString(3, targetCurrencyCode);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return extractEntity(resultSet);
            } else {
                throw createExceptionForEmptyResultSet("update", baseCurrencyCode, targetCurrencyCode);
            }
        } catch (SQLException e) {
            throw createDatabaseException("update", baseCurrencyCode, targetCurrencyCode);
        }
    }

    @Override
    protected Collection<EntityExchangeRate> extractEntities(ResultSet resultSet) throws SQLException {
        Collection<EntityExchangeRate> exchangeRates = new ArrayList<>();
        while (resultSet.next()) {
            exchangeRates.add(extractEntity(resultSet));
        }

        return exchangeRates;
    }

    @Override
    protected EntityExchangeRate extractEntity(ResultSet resultSet) throws SQLException {
        return EntityExchangeRate.builder()
                .id(resultSet.getInt("exchange_rate_id"))
                .baseCurrency(EntityCurrency.builder()
                        .id(resultSet.getInt("base_currency_id"))
                        .name(resultSet.getString("base_currency_name"))
                        .code(resultSet.getString("base_currency_code"))
                        .sign(resultSet.getString("base_currency_sign"))
                        .build())
                .targetCurrency(EntityCurrency.builder()
                        .id(resultSet.getInt("target_currency_id"))
                        .name(resultSet.getString("target_currency_name"))
                        .code(resultSet.getString("target_currency_code"))
                        .sign(resultSet.getString("target_currency_sign"))
                        .build())
                .rate(resultSet.getBigDecimal("rate"))
                .build();
    }

    @Override
    protected CERuntimeException createAlreadyExistsException(Object... params) {
        return new CEAlreadyExists(String.format(ExceptionMessage.ALREADY_EXISTS, String.format(EXCHANGE_RATE, params[2], params[1])));
    }

    @Override
    protected CERuntimeException createExceptionForEmptyResultSet(String operation, Object... params) {
        String details;

        switch (operation) {
            case SAVE_OPERATION -> details = String.format(EXCHANGE_RATE + ": currency not found", params[1], params[2]);
            case UPDATE_OPERATION -> throw new CENotFoundException(String.format(ExceptionMessage.FAILED_OPERATION, operation,
                                    String.format("rate: " + EXCHANGE_RATE + " not found", params[0], params[1])));
            default -> throw new CEDatabaseException(ExceptionMessage.ERROR_PROCESSING_REQUEST_TO_DATABASE);
        }

        return new CEDatabaseException(String.format(ExceptionMessage.FAILED_OPERATION, operation, details));
    }

    @Override
    protected CERuntimeException createDatabaseException(String operation, Object... params) {
        String details;

        switch (operation) {
            case SAVE_OPERATION -> details = String.format(EXCHANGE_RATE + " to the database", params[1], params[2]);
            case READ_OPERATION -> details = String.format(EXCHANGE_RATE + " from the database", params[0], params[1]);
            case READ_ALL_OPERATION -> details = "exchange rates from the database";
            case UPDATE_OPERATION -> details = String.format(EXCHANGE_RATE + " in the database", params[0], params[1]);
            default -> throw new CEDatabaseException(ExceptionMessage.ERROR_PROCESSING_REQUEST_TO_DATABASE);
        }

        return new CEDatabaseException(String.format(ExceptionMessage.FAILED_OPERATION, operation, details));
    }
}
