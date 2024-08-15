package com.asalavei.currencyexchange.api.dbaccess.dao;

import com.asalavei.currencyexchange.api.dbaccess.entities.EntityCurrency;
import com.asalavei.currencyexchange.api.dbaccess.util.ConnectionUtil;
import com.asalavei.currencyexchange.api.exceptions.CEAlreadyExists;
import com.asalavei.currencyexchange.api.exceptions.CEDatabaseUnavailableException;
import com.asalavei.currencyexchange.api.exceptions.CENotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class JdbcCurrencyDao implements CurrencyDao {
    @Override
    public EntityCurrency save(EntityCurrency entity) {
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO currencies (code, full_name, sign) VALUES (?, ?, ?)"
             )) {
            preparedStatement.setString(1, entity.getCode());
            preparedStatement.setString(2, entity.getFullName());
            preparedStatement.setString(3, entity.getSign());
            preparedStatement.executeUpdate();

            return findByCode(entity.getCode());
        } catch (SQLException e) {
            if (e.getSQLState().startsWith("23")) {
                throw new CEAlreadyExists("Currency with '" + entity.getCode() + "' code already exists.");
            }
            throw new CEDatabaseUnavailableException("Database is unavailable or an error occurred while processing the request. " + e);
        }
    }

    @Override
    public Collection<EntityCurrency> findAll() {
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM currencies");
             ResultSet resultSet = preparedStatement.executeQuery()) {
            Collection<EntityCurrency> currencies = new ArrayList<>();

            while (resultSet.next()) {
                EntityCurrency entityCurrency = EntityCurrency.builder()
                        .id(resultSet.getInt("id"))
                        .code(resultSet.getString("code"))
                        .fullName(resultSet.getString("full_name"))
                        .sign(resultSet.getString("sign"))
                        .build();

                currencies.add(entityCurrency);
            }

            return currencies;
        } catch (SQLException e) {
            throw new CEDatabaseUnavailableException("Database is unavailable or an error occurred while processing the request. " + e);
        }
    }

    @Override
    public EntityCurrency findById(Integer id) {
        return findBy("SELECT * FROM currencies WHERE id = ?", id);
    }

    @Override
    public EntityCurrency findByCode(String code) {
        return findBy("SELECT * FROM currencies WHERE code = ?", code);
    }

    public <T> EntityCurrency findBy(String query, T parameter) {
        EntityCurrency entityCurrency = null;

        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            if (parameter instanceof Integer) {
                preparedStatement.setInt(1, (Integer) parameter);
            } else if (parameter instanceof String) {
                preparedStatement.setString(1, (String) parameter);
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                entityCurrency = EntityCurrency.builder()
                        .id(resultSet.getInt("id"))
                        .code(resultSet.getString("code"))
                        .fullName(resultSet.getString("full_name"))
                        .sign(resultSet.getString("sign"))
                        .build();
            } else {
                throw new CENotFoundException("Not found Currency with " + (parameter instanceof Integer ? "id" : "code") + " = '" + parameter + "'");
            }
        } catch (SQLException e) {
            throw new CEDatabaseUnavailableException("Database is unavailable or an error occurred while processing the request. " + e);
        }

        return entityCurrency;
    }
}
