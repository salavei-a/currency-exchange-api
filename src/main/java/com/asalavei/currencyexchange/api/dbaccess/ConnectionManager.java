package com.asalavei.currencyexchange.api.dbaccess;

import com.asalavei.currencyexchange.api.exceptions.CEDatabaseException;
import com.asalavei.currencyexchange.api.exceptions.ExceptionMessage;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariPool;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionManager {

    private static HikariDataSource hikariDataSource;

    private ConnectionManager() {
    }

    public static HikariDataSource getHikariDataSource() {
        try {
            initDataSource();
            return hikariDataSource;
        } catch (HikariPool.PoolInitializationException e) {
            throw new CEDatabaseException(ExceptionMessage.DATABASE_UNAVAILABLE, e);
        }
    }

    public static Connection getConnection() {
        try {
            initDataSource();
            return hikariDataSource.getConnection();
        } catch (HikariPool.PoolInitializationException | SQLException e) {
            throw new CEDatabaseException(ExceptionMessage.DATABASE_UNAVAILABLE, e);
        }
    }

    private static void initDataSource() {
        if (hikariDataSource == null) {
            HikariConfig config = new HikariConfig();

            config.setJdbcUrl(DatabaseConfig.URL);
            config.setUsername(DatabaseConfig.USER);
            config.setPassword(DatabaseConfig.PASSWORD);
            config.setDriverClassName(DatabaseConfig.DRIVER_CLASS_NAME);
            config.setMaximumPoolSize(10);

            hikariDataSource = new HikariDataSource(config);
        }
    }
}
