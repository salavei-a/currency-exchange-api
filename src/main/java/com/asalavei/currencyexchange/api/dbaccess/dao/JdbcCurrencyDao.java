package com.asalavei.currencyexchange.api.dbaccess.dao;

import com.asalavei.currencyexchange.api.dbaccess.entities.EntityCurrency;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcCurrencyDao implements CurrencyDao {
    @Override
    public List<EntityCurrency> findAll() {
        List<EntityCurrency> currencies = new ArrayList<>();

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/currency_exchange",
                    "postgres", ""
            );

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * from currencies");

            while (resultSet.next()) {
                EntityCurrency entityCurrency = EntityCurrency.builder()
                        .id(resultSet.getInt("id"))
                        .code(resultSet.getString("code"))
                        .fullName(resultSet.getString("full_name"))
                        .sign(resultSet.getString("sign"))
                        .build();

                currencies.add(entityCurrency);
            }

            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return currencies;
    }
}
