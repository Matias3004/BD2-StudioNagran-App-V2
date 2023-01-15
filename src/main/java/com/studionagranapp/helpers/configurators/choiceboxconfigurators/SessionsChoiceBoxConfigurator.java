package com.studionagranapp.helpers.configurators.choiceboxconfigurators;

import com.studionagranapp.helpers.databaseconnection.DatabaseManager;
import javafx.scene.control.ChoiceBox;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SessionsChoiceBoxConfigurator {

    public void initValues(String query, ChoiceBox<String> sessionChoiceBox) {
        try {
            DatabaseManager databaseManager = DatabaseManager.getInstance();
            ResultSet sessions = databaseManager.executeQuery(query);

            while (sessions.next()) {
                if (!sessionChoiceBox.getItems().contains(sessionRepresentation(sessions)))
                    sessionChoiceBox.getItems().add(sessionRepresentation(sessions));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    private String sessionRepresentation(ResultSet sessions) throws SQLException {
        return sessions.getString("session_name");
    }
}
