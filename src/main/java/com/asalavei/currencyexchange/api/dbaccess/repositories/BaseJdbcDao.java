package com.asalavei.currencyexchange.api.dbaccess.repositories;

import com.asalavei.currencyexchange.api.dbaccess.entities.Entity;
import com.asalavei.currencyexchange.api.dbaccess.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public abstract class BaseJdbcDao<E extends Entity> {

    protected Connection getConnection() throws SQLException {
        return ConnectionUtil.getConnection();
    }

    protected Collection<E> findAll(String query) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            return extractEntities(resultSet);
        }
    }

    protected abstract E extractEntity(ResultSet resultSet) throws SQLException;

    protected abstract Collection<E> extractEntities(ResultSet resultSet) throws SQLException;
}
