package com.studionagranapp.helpers.configurators.tableconfigurators;

import com.studionagranapp.helpers.databaseconnection.DatabaseManager;
import com.studionagranapp.helpers.models.User;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientsTableConfigurator {

    public void provideConfiguration(ObservableList<User> clientsObservableList,
                                     TableView<User> clientsTable) {
        try {
            DatabaseManager databaseManager = DatabaseManager.getInstance();
            clientsObservableList.clear();

            String query = "SELECT * FROM User_accounts WHERE role = 'CLIENT'";
            ResultSet clients = databaseManager.executeQuery(query);

            while (clients.next()) {
                clientsObservableList.add(new User(
                        clients.getInt("account_id"),
                        clients.getString("first_name")
                            + " " + clients.getString("last_name"),
                        clients.getString("username"),
                        clients.getString("password"),
                        clients.getString("role"),
                        clients.getString("email"),
                        clients.getString("phone_number")
                ));
                clientsTable.setItems(clientsObservableList);
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }
}
