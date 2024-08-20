package com.asalavei.currencyexchange.api.dbaccess.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {
    private static final String URL = "jdbc:postgresql://localhost:4679/currency_exchange";
    private static final String USER = System.getenv("POSTGRES_USER") != null ? System.getenv("POSTGRES_USER") : "admin";
    private static final String PASSWORD = System.getenv("POSTGRES_PASSWORD") != null ? System.getenv("POSTGRES_PASSWORD") : "admin";

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private ConnectionUtil() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
