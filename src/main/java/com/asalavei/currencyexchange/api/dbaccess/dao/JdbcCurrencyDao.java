package com.asalavei.currencyexchange.api.dbaccess.dao;

import com.asalavei.currencyexchange.api.dbaccess.entities.EntityCurrency;
import com.asalavei.currencyexchange.api.dbaccess.util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcCurrencyDao implements CurrencyDao {
    @Override
    public void save(EntityCurrency entity) {
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO currencies (code, full_name, sign) VALUES (?, ?, ?)"
             )) {

            preparedStatement.setString(1, entity.getCode());
            preparedStatement.setString(2, entity.getFullName());
            preparedStatement.setString(3, entity.getSign());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<EntityCurrency> findAll() {
        List<EntityCurrency> currencies = new ArrayList<>();

        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM currencies");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                EntityCurrency entityCurrency = EntityCurrency.builder()
                        .id(resultSet.getInt("id"))
                        .code(resultSet.getString("code"))
                        .fullName(resultSet.getString("full_name"))
                        .sign(resultSet.getString("sign"))
                        .build();

                currencies.add(entityCurrency);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return currencies;
    }

    @Override
    public EntityCurrency findByCode(String code) {
        EntityCurrency entityCurrency = null;

        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM currencies WHERE code = ?")) {

            preparedStatement.setString(1, code);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                entityCurrency = EntityCurrency.builder()
                        .id(resultSet.getInt("id"))
                        .code(resultSet.getString("code"))
                        .fullName(resultSet.getString("full_name"))
                        .sign(resultSet.getString("sign"))
                        .build();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entityCurrency;
    }
}
