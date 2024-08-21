package com.asalavei.currencyexchange.api.dbaccess.repositories;

import com.asalavei.currencyexchange.api.dbaccess.entities.EntityCurrency;
import com.asalavei.currencyexchange.api.dbaccess.util.ConnectionUtil;
import com.asalavei.currencyexchange.api.exceptions.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class JdbcCurrencyDao extends BaseJdbcDao<EntityCurrency> implements CurrencyRepository {

    @Override
    public EntityCurrency save(EntityCurrency entity) {
        return save("INSERT INTO currencies (full_name, code, sign) VALUES (?, ?, ?) RETURNING id, full_name, code, sign",
                entity.getName(), entity.getCode(), entity.getSign());
    }

    @Override
    public Collection<EntityCurrency> findAll() {
        try {
            return findAll("SELECT id, full_name, code, sign FROM currencies");
        } catch (SQLException e) {
            throw new CEDatabaseUnavailableException(ExceptionMessages.DATABASE_UNAVAILABLE, e);
        }
    }

    @Override
    public EntityCurrency findByCode(String code) {
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, full_name, code, sign FROM currencies WHERE code = ?")) {
            preparedStatement.setString(1, code);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return extractEntity(resultSet);
            } else {
                throw new CENotFoundException(ExceptionMessages.CURRENCY_NOT_FOUND, " with the code '" + code +"'");
            }
        } catch (SQLException e) {
            throw new CEDatabaseUnavailableException(ExceptionMessages.DATABASE_UNAVAILABLE, e);
        }
    }

    @Override
    public Integer getIdByCode(String code) {
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM currencies WHERE code = ?")) {
            preparedStatement.setString(1, code);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("id");
            } else {
                throw new CENotFoundException(String.format(ExceptionMessages.CURRENCY_NOT_FOUND, " with the code '" + code +"'"));
            }
        } catch (SQLException e) {
            throw new CEDatabaseUnavailableException(ExceptionMessages.DATABASE_UNAVAILABLE, e);
        }
    }

    @Override
    protected EntityCurrency extractEntity(ResultSet resultSet) throws SQLException {
        return EntityCurrency.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("full_name"))
                .code(resultSet.getString("code"))
                .sign(resultSet.getString("sign"))
                .build();
    }

    @Override
    protected Collection<EntityCurrency> extractEntities(ResultSet resultSet) throws SQLException {
        Collection<EntityCurrency> currencies = new ArrayList<>();
        while (resultSet.next()) {
            currencies.add(extractEntity(resultSet));
        }

        return currencies;
    }
}
