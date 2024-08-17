package com.asalavei.currencyexchange.api.dbaccess.repositories;

import com.asalavei.currencyexchange.api.dbaccess.entities.EntityCurrency;
import com.asalavei.currencyexchange.api.dbaccess.entities.EntityExchangeRate;
import com.asalavei.currencyexchange.api.dbaccess.util.ConnectionUtil;
import com.asalavei.currencyexchange.api.exceptions.CEAlreadyExists;
import com.asalavei.currencyexchange.api.exceptions.CEDatabaseUnavailableException;
import com.asalavei.currencyexchange.api.exceptions.CENotFoundException;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class JdbcExchangeRateDao implements ExchangeRateRepository {

    private final CurrencyRepository currencyRepository;

    public JdbcExchangeRateDao(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Override
    public EntityExchangeRate save(EntityExchangeRate entity) {
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO exchange_rates (base_currency_id, target_currency_id, rate) VALUES (?, ?, ?) RETURNING id"
             )) {
            EntityCurrency baseCurrency = entity.getBaseCurrency();
            EntityCurrency targetCurrency = entity.getTargetCurrency();
            BigDecimal rate = entity.getRate();

            preparedStatement.setObject(1, baseCurrency.getId(), Types.INTEGER);
            preparedStatement.setObject(2, targetCurrency.getId(), Types.INTEGER);
            preparedStatement.setBigDecimal(3, rate);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Integer id = resultSet.getInt(1);

                return EntityExchangeRate.builder()
                        .id(id)
                        .baseCurrency(baseCurrency)
                        .targetCurrency(targetCurrency)
                        .rate(rate)
                        .build();
            } else {
                throw new CEDatabaseUnavailableException("Failed to retrieve generated ID.");
            }
        } catch (SQLException e) {
            if (e.getSQLState().startsWith("23")) {
                throw new CEAlreadyExists("Exchange rate for this currency pair already exists.");
            }
            throw new CEDatabaseUnavailableException("Database is unavailable or an error occurred while processing the request. " + e);
        }
    }

    @Override
    public Collection<EntityExchangeRate> findAll() {
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM exchange_rates");
             ResultSet resultSet = preparedStatement.executeQuery()) {
            Collection<EntityExchangeRate> exchangeRates = new ArrayList<>();

            while (resultSet.next()) {
                EntityExchangeRate entityExchangeRate = EntityExchangeRate.builder()
                        .id(resultSet.getInt("id"))
                        .baseCurrency(currencyRepository.findById(resultSet.getInt("base_currency_id")))
                        .targetCurrency(currencyRepository.findById(resultSet.getInt("target_currency_id")))
                        .rate(resultSet.getBigDecimal("rate"))
                        .build();

                exchangeRates.add(entityExchangeRate);
            }

            return exchangeRates;
        } catch (SQLException e) {
            throw new CEDatabaseUnavailableException("Database is unavailable or an error occurred while processing the request. " + e);
        }
    }

    @Override
    public EntityExchangeRate findByCurrencyPair(Integer idBaseCurrency, Integer idTargetCurrency) {
        String query = "SELECT * FROM exchange_rates WHERE (base_currency_id, target_currency_id) = (?, ?)";

        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, idBaseCurrency, Types.INTEGER);
            preparedStatement.setObject(2, idTargetCurrency, Types.INTEGER);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return EntityExchangeRate.builder()
                        .id(resultSet.getInt("id"))
                        .baseCurrency(currencyRepository.findById(resultSet.getInt("base_currency_id")))
                        .targetCurrency(currencyRepository.findById(resultSet.getInt("target_currency_id")))
                        .rate(resultSet.getBigDecimal("rate"))
                        .build();
            } else {
                throw new CENotFoundException("Not found exchange rate for this currency pair.");
            }
        } catch (SQLException e) {
            throw new CEDatabaseUnavailableException("Database is unavailable or an error occurred while processing the request. " + e);
        }
    }

    @Override
    public BigDecimal getRateByCurrencyPair(Integer idBaseCurrency, Integer idTargetCurrency) {
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT rate FROM exchange_rates WHERE (base_currency_id, target_currency_id) = (?, ?)")) {
            preparedStatement.setObject(1, idBaseCurrency, Types.INTEGER);
            preparedStatement.setObject(2, idTargetCurrency, Types.INTEGER);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getBigDecimal("rate");
            } else {
                throw new CENotFoundException("Not found exchange rate for this currency pair.");
            }
        } catch (SQLException e) {
            throw new CEDatabaseUnavailableException("Database is unavailable or an error occurred while processing the request. " + e);
        }
    }

    @Override
    public EntityExchangeRate update(BigDecimal rate, Integer idBaseCurrency, Integer idTargetCurrency) {
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE exchange_rates SET rate = ? WHERE (base_currency_id, target_currency_id) = (?, ?) RETURNING id")) {
            preparedStatement.setBigDecimal(1, rate);
            preparedStatement.setObject(2, idBaseCurrency, Types.INTEGER);
            preparedStatement.setObject(3, idTargetCurrency, Types.INTEGER);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Integer idExchangeRate = resultSet.getObject(1, Integer.class);

                return EntityExchangeRate.builder()
                        .id(idExchangeRate)
                        .baseCurrency(currencyRepository.findById(idBaseCurrency))
                        .targetCurrency(currencyRepository.findById(idTargetCurrency))
                        .rate(rate)
                        .build();
            } else {
                throw new CENotFoundException("Not found exchange rate for this currency pair.");
            }
        } catch (SQLException e) {
            throw new CEDatabaseUnavailableException("Database is unavailable or an error occurred while processing the request. " + e);
        }
    }
}
