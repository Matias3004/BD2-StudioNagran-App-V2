package com.studionagranapp.helpers.databaseconnection;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnector {

    public Connection connect() {
        Connection connection = null;
        final String DATABASE_NAME = "BD2-LocalDB";
        String url = "jdbc:mysql://localhost/" + DATABASE_NAME;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            final String DATABASE_USER = "root";
            final String DATABSE_PASSWORD = "";
            connection = DriverManager.getConnection(url, DATABASE_USER, DATABSE_PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

        return connection;
    }
}
