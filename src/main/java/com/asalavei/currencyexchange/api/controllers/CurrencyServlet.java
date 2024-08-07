package com.asalavei.currencyexchange.api.controllers;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class CurrencyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/currency-exchange",
                    "postgres", ""
            );

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT code, full_name, sign from currencies");

            while (resultSet.next()) {
                out.println(resultSet.getString("code"));
                out.println(resultSet.getString("full_name"));
                out.println(resultSet.getString("sign"));
                out.println("\n");
            }

            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
    }
}
