package com.studionagranapp.helpers.configurators.choiceboxconfigurators;

import com.studionagranapp.helpers.databaseconnection.DatabaseManager;
import javafx.scene.control.ChoiceBox;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EngineersChoiceBoxConfigurator {

    public void initValues(String query, ChoiceBox<String> engineerChoiceBox) {
        try {
            DatabaseManager databaseManager = DatabaseManager.getInstance();
            ResultSet engineers = databaseManager.executeQuery(query);

            while (engineers.next()) {
                if (!engineerChoiceBox.getItems().contains(engineerRepresentation(engineers)))
                    engineerChoiceBox.getItems().add(engineerRepresentation(engineers));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    private String engineerRepresentation(ResultSet engineers) throws SQLException {
        return engineers.getString("first_name") + " "
                + engineers.getString("last_name");
    }
}
