package com.studionagranapp.helpers.configurators.tableconfigurators;

import com.studionagranapp.helpers.databaseconnection.DatabaseManager;
import com.studionagranapp.helpers.models.User;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EngineersTableConfigurator {

    public void provideConfiguration(ObservableList<User> engineersObservableList,
                           TableView<User> engineersTable) {
        try {
            DatabaseManager databaseManager = DatabaseManager.getInstance();
            engineersObservableList.clear();

            String query = "SELECT * FROM Studio_nagran.Engineers";
            ResultSet engineers = databaseManager.executeQuery(query);

            while (engineers.next()) {
                engineersObservableList.add(new User(
                        engineers.getInt("account_id"),
                        engineers.getString("first_name"),
                        engineers.getString("last_name"),
                        engineers.getString("username"),
                        engineers.getString("password"),
                        engineers.getString("role"),
                        engineers.getString("email"),
                        engineers.getString("phone_number")
                ));
                engineersTable.setItems(engineersObservableList);
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }
}
