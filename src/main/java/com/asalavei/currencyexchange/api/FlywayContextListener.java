package com.asalavei.currencyexchange.api;

import com.asalavei.currencyexchange.api.dbaccess.ConnectionManager;
import com.asalavei.currencyexchange.api.exceptions.CEDatabaseException;
import com.asalavei.currencyexchange.api.exceptions.CERuntimeException;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class FlywayContextListener implements ServletContextListener {

    private static final Logger logger = Logger.getLogger(FlywayContextListener.class.getName());

    public static final String ERROR_FLYWAY = "Failed to execute Flyway migration";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            Flyway flyway = Flyway.configure()
                    .dataSource(ConnectionManager.getHikariDataSource())
                    .load();

            flyway.migrate();
            logger.info("Flyway migration completed successfully");
        } catch (FlywayException | CEDatabaseException e) {
            logger.log(Level.SEVERE, ERROR_FLYWAY, e);
            throw new CERuntimeException(ERROR_FLYWAY);
        }
    }
}
