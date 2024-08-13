package com.asalavei.currencyexchange.api.dbaccess.dao;

import com.asalavei.currencyexchange.api.dbaccess.entities.EntityExchangeRate;
import com.asalavei.currencyexchange.api.dbaccess.util.ConnectionUtil;
import com.asalavei.currencyexchange.api.exceptions.CEDatabaseUnavailableException;
import com.asalavei.currencyexchange.api.exceptions.CENotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class JdbcExchangeRateDao implements ExchangeRateDao {

    private final CurrencyDao currencyDao;

    public JdbcExchangeRateDao(CurrencyDao currencyDao) {
        this.currencyDao = currencyDao;
    }

    @Override
    public EntityExchangeRate save(EntityExchangeRate entity) {
        return null; //TODO
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
                        .baseCurrency(currencyDao.findById(resultSet.getInt("base_currency_id")))
                        .targetCurrency(currencyDao.findById(resultSet.getInt("target_currency_id")))
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
        String query = "SELECT * FROM exchange_rates WHERE base_currency_id = " + idBaseCurrency + " AND target_currency_id = " + idTargetCurrency;
        EntityExchangeRate entityExchangeRate = null;

        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                entityExchangeRate = EntityExchangeRate.builder()
                        .id(resultSet.getInt("id"))
                        .baseCurrency(currencyDao.findById(resultSet.getInt("base_currency_id")))
                        .targetCurrency(currencyDao.findById(resultSet.getInt("target_currency_id")))
                        .rate(resultSet.getBigDecimal("rate"))
                        .build();
            } else {
                throw new CENotFoundException("Not found exchange rate for this currency pair.");
            }
        } catch (SQLException e) {
            throw new CEDatabaseUnavailableException("Database is unavailable or an error occurred while processing the request. " + e);
        }

        return entityExchangeRate;
    }

    @Override
    public EntityExchangeRate findByCode(String code) {
        return null; //TODO
    }
}
