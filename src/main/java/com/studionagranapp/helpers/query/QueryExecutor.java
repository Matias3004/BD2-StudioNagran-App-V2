package com.studionagranapp.helpers.query;

import com.studionagranapp.helpers.databaseconnection.DatabaseConnector;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class QueryExecutor {

    private final DatabaseConnector databaseConnector;

    public QueryExecutor() {
        this.databaseConnector = new DatabaseConnector();
    }

    public ResultSet executeQuery(String query) {
        Connection connectDB = databaseConnector.connect();
        try {
            Statement statement = connectDB.createStatement();

            return statement.executeQuery(query);
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

        return null;
    }
}
