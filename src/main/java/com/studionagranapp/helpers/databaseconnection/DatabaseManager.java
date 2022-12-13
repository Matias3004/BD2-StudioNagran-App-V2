package com.studionagranapp.helpers.databaseconnection;

import com.studionagranapp.helpers.errorhandling.AlertManager;
import com.studionagranapp.helpers.query.QueryExecutor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseManager {

    private static DatabaseManager databaseManager;
    private final DatabaseConnector databaseConnector;
    private final QueryExecutor queryExecutor;
    private final AlertManager alertManager;

    private DatabaseManager() {
        databaseConnector = new DatabaseConnector();
        queryExecutor = new QueryExecutor();
        alertManager = new AlertManager();
    }

    public static DatabaseManager getInstance() {
        if (databaseManager == null)
            databaseManager = new DatabaseManager();

        return databaseManager;
    }

    public ResultSet executeQuery(String query) {
        return queryExecutor.executeQuery(query);
    }

    public Connection getConnection() {
        return databaseConnector.connect();
    }

    private DatabaseResponse performDeletion(String query) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.execute();

            return DatabaseResponse.SUCCESS;
        } catch (SQLException ex) {
            ex.printStackTrace();
            ex.getCause();

            return DatabaseResponse.ERROR;
        }
    }
}
