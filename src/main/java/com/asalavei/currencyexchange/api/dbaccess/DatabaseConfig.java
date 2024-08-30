package com.asalavei.currencyexchange.api.dbaccess;

public class DatabaseConfig {
    public static final String URL = System.getenv("POSTGRES_URL") != null ? System.getenv( "POSTGRES_URL") : "jdbc:postgresql://localhost:4679/currency_exchange";
    public static final String USER = System.getenv("POSTGRES_USER") != null ? System.getenv("POSTGRES_USER") : "admin";
    public static final String PASSWORD = System.getenv("POSTGRES_PASSWORD") != null ? System.getenv("POSTGRES_PASSWORD") : "admin";
    public static final String DRIVER_CLASS_NAME = "org.postgresql.Driver";

    private DatabaseConfig() {
    }
}
