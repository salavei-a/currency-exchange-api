package com.asalavei.currencyexchange.api.dbaccess;

import com.asalavei.currencyexchange.api.exceptions.CEDatabaseException;
import com.asalavei.currencyexchange.api.exceptions.ExceptionMessage;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariPool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionManager {

    private static final Logger logger = Logger.getLogger(ConnectionManager.class.getName());

    private static HikariDataSource hikariDataSource;

    private ConnectionManager() {
    }

    public static HikariDataSource getHikariDataSource() {
        initDataSource();
        return hikariDataSource;
    }

    public static Connection getConnection() {
        try {
            initDataSource();
            return hikariDataSource.getConnection();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to obtain a database connection", e);
            throw new CEDatabaseException(ExceptionMessage.DATABASE_UNAVAILABLE);
        }
    }

    private static void initDataSource() {
        if (hikariDataSource == null) {
            try {
                HikariConfig config = new HikariConfig();

                config.setJdbcUrl(DatabaseConfig.URL);
                config.setUsername(DatabaseConfig.USER);
                config.setPassword(DatabaseConfig.PASSWORD);
                config.setDriverClassName(DatabaseConfig.DRIVER_CLASS_NAME);
                config.setMaximumPoolSize(10);

                hikariDataSource = new HikariDataSource(config);
                logger.info("DataSource initialized successfully");
            } catch (HikariPool.PoolInitializationException e) {
                logger.log(Level.SEVERE, "Failed to initialize DataSource", e);
                throw new CEDatabaseException(ExceptionMessage.DATABASE_UNAVAILABLE);
            }
        }
    }
}
