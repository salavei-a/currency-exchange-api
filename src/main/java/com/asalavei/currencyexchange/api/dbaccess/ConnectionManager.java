package com.asalavei.currencyexchange.api.dbaccess;

import com.asalavei.currencyexchange.api.exceptions.CEDatabaseException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariPool;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionManager {

    private static HikariDataSource hikariDataSource;

    private static final String URL = "jdbc:postgresql://localhost:4679/currency_exchange";
    private static final String USER = System.getenv("POSTGRES_USER") != null ? System.getenv("POSTGRES_USER") : "admin";
    private static final String PASSWORD = System.getenv("POSTGRES_PASSWORD") != null ? System.getenv("POSTGRES_PASSWORD") : "admin";

    private ConnectionManager() {
    }

    public static Connection getConnection() {
        try {
            initDataSource();
            return hikariDataSource.getConnection();
        } catch (HikariPool.PoolInitializationException | SQLException e) {
            throw new CEDatabaseException("Database unavailable", e);
        }
    }

    private static void initDataSource() {
        if (hikariDataSource == null) {
            HikariConfig config = new HikariConfig();

            config.setJdbcUrl(URL);
            config.setUsername(USER);
            config.setPassword(PASSWORD);
            config.setDriverClassName("org.postgresql.Driver");
            config.setMaximumPoolSize(10);

            hikariDataSource = new HikariDataSource(config);
        }
    }
}
