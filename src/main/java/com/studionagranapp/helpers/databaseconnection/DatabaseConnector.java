package com.studionagranapp.helpers.databaseconnection;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnector {

    public Connection connect() {
        Connection connection = null;
        final String SCHEMA_NAME = "recordingstudio";
        String url = "jdbc:postgresql://localhost:5432/postgres?currentSchema=" + SCHEMA_NAME;
        try {
            Class.forName("org.postgresql.Driver");
            final String DATABASE_USER = "postgres";
            final String DATABSE_PASSWORD = "password";
            connection = DriverManager.getConnection(url, DATABASE_USER, DATABSE_PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

        return connection;
    }
}
