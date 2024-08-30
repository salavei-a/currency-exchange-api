package com.asalavei.currencyexchange.api;

import com.asalavei.currencyexchange.api.dbaccess.ConnectionManager;
import com.asalavei.currencyexchange.api.exceptions.CEDatabaseException;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ServletContextListener that triggers Flyway migrations when the application starts
 */
public class FlywayContextListener implements ServletContextListener {

    private static final Logger logger = Logger.getLogger(FlywayContextListener.class.getName());

    public static final String ERROR_FLYWAY = "Failed to execute Flyway migration";

    /**
     * Initializes the Flyway migration process when the servlet context is initialized
     *
     * @param sce the ServletContextEvent containing the servlet context
     */
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
        }
    }
}
