package com.asalavei.currencyexchange.api.dbaccess.repositories;

import com.asalavei.currencyexchange.api.dbaccess.entities.EntityCurrency;
import com.asalavei.currencyexchange.api.dbaccess.entities.EntityExchangeRate;
import com.asalavei.currencyexchange.api.dbaccess.util.ConnectionUtil;
import com.asalavei.currencyexchange.api.exceptions.*;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class JdbcExchangeRateDao implements ExchangeRateRepository {

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
                       "bc.id AS base_currency_id, bc.full_name AS base_currency_name, bc.code AS base_currency_code,  bc.sign AS base_currency_sign, " +
                       "tc.id AS target_currency_id, tc.full_name AS target_currency_name, tc.code AS target_currency_code, tc.sign AS target_currency_sign " +
                       "FROM inserted i " +
                       "JOIN currencies bc ON i.base_currency_id = bc.id " +
                       "JOIN currencies tc ON i.target_currency_id = tc.id;";

        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setBigDecimal(1, entity.getRate());
            preparedStatement.setString(2, entity.getBaseCurrency().getCode());
            preparedStatement.setString(3, entity.getTargetCurrency().getCode());

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return getEntityExchangeRate(resultSet);
            } else {
                throw new CENotFoundException(ExceptionMessages.CURRENCY_NOT_FOUND, " required to save the exchange rate");
            }
        } catch (SQLException e) {
            if (e.getSQLState().startsWith("23")) {
                throw new CEAlreadyExists("Exchange rate for this currency pair already exists");
            }
            throw new CEDatabaseUnavailableException(ExceptionMessages.DATABASE_UNAVAILABLE, e);
        }
    }

    @Override
    public Collection<EntityExchangeRate> findAll() {
        String query = "SELECT er.id AS exchange_rate_id, er.rate AS rate, " +
                       "bc.id AS base_currency_id, bc.full_name AS base_currency_name, bc.code AS base_currency_code, bc.sign AS base_currency_sign, " +
                       "tc.id AS target_currency_id, tc.full_name AS target_currency_name, tc.code AS target_currency_code, tc.sign AS target_currency_sign " +
                       "FROM exchange_rates er " +
                       "JOIN currencies bc ON er.base_currency_id = bc.id " +
                       "JOIN currencies tc ON er.target_currency_id = tc.id";

        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            Collection<EntityExchangeRate> exchangeRates = new ArrayList<>();

            while (resultSet.next()) {
                exchangeRates.add(getEntityExchangeRate(resultSet));
            }

            return exchangeRates;
        } catch (SQLException e) {
            throw new CEDatabaseUnavailableException(ExceptionMessages.DATABASE_UNAVAILABLE, e);
        }
    }

    @Override
    public EntityExchangeRate findByCurrencyPairCodes(String baseCurrencyCode, String targetCurrencyCode) {
        String query = "SELECT er.id AS exchange_rate_id, er.rate AS rate, " +
                       "bc.id AS base_currency_id, bc.full_name AS base_currency_name, bc.code AS base_currency_code, bc.sign AS base_currency_sign, " +
                       "tc.id AS target_currency_id, tc.full_name AS target_currency_name, tc.code AS target_currency_code, tc.sign AS target_currency_sign " +
                       "FROM exchange_rates er " +
                       "JOIN currencies bc ON er.base_currency_id = bc.id " +
                       "JOIN currencies tc ON er.target_currency_id = tc.id " +
                       "WHERE (bc.code, tc.code) = (?, ?)";

        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, baseCurrencyCode);
            preparedStatement.setString(2, targetCurrencyCode);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return getEntityExchangeRate(resultSet);
            } else {
                throw new CENotFoundException(ExceptionMessages.EXCHANGE_RATE_NOT_FOUND);
            }
        } catch (SQLException e) {
            throw new CEDatabaseUnavailableException(ExceptionMessages.DATABASE_UNAVAILABLE, e);
        }
    }

    @Override
    public BigDecimal getRateByCurrencyPairIds(Integer baseCurrencyId, Integer targetCurrencyId) {
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT rate FROM exchange_rates WHERE (base_currency_id, target_currency_id) = (?, ?)")) {
            preparedStatement.setInt(1, baseCurrencyId);
            preparedStatement.setInt(2, targetCurrencyId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getBigDecimal("rate");
            } else {
                throw new CENotFoundException(ExceptionMessages.EXCHANGE_RATE_NOT_FOUND);
            }
        } catch (SQLException e) {
            throw new CEDatabaseUnavailableException(ExceptionMessages.DATABASE_UNAVAILABLE, e);
        }
    }

    @Override
    public EntityExchangeRate updateRate(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) {
        String query = "UPDATE exchange_rates er " +
                       "SET rate = ? " +
                       "FROM currencies bc, currencies tc " +
                       "WHERE (er.base_currency_id, er.target_currency_id) = (bc.id, tc.id) " +
                       "AND (bc.code, tc.code) = (?, ?) " +
                       "RETURNING er.id AS exchange_rate_id, er.rate AS rate, " +
                       "bc.id AS base_currency_id, bc.full_name AS base_currency_name, bc.code AS base_currency_code, bc.sign AS base_currency_sign, " +
                       "tc.id AS target_currency_id, tc.full_name AS target_currency_name, tc.code AS target_currency_code, tc.sign AS target_currency_sign";

        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setBigDecimal(1, rate);
            preparedStatement.setString(2, baseCurrencyCode);
            preparedStatement.setString(3, targetCurrencyCode);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return getEntityExchangeRate(resultSet);
            } else {
                throw new CENotFoundException(ExceptionMessages.EXCHANGE_RATE_NOT_FOUND);
            }
        } catch (SQLException e) {
            throw new CEDatabaseUnavailableException(ExceptionMessages.DATABASE_UNAVAILABLE, e);
        }
    }

    private EntityExchangeRate getEntityExchangeRate(ResultSet resultSet) throws SQLException {
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
}
