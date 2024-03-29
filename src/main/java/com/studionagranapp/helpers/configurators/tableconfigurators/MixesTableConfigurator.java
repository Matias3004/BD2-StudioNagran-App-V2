package com.studionagranapp.helpers.configurators.tableconfigurators;

import com.studionagranapp.helpers.databaseconnection.DatabaseManager;
import com.studionagranapp.helpers.models.Mix;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MixesTableConfigurator {

    public void provideEngineerConfiguration(ObservableList<Mix> mixesObservableList,
                                             TableView<Mix> mixesTable, Integer id) {
        try {
            DatabaseManager databaseManager = DatabaseManager.getInstance();
            mixesObservableList.clear();

            String query = "SELECT Mixes.id, " +
                    "Mixes.filename, " +
                    "Mixes.upload_date, " +
                    "Mixes.path, " +
                    "Session.session_name, " +
                    "Session.band_name " +
                    "FROM Mixes " +
                    "JOIN Sessions as Session " +
                    "ON Session.id = Mixes.Session_id " +
                    "WHERE Session.Engineer_id = " + id;
            ResultSet mixes = databaseManager.executeQuery(query);

            while (mixes.next()) {
                mixesObservableList.add(new Mix(
                        mixes.getInt("id"),
                        mixes.getString("filename"),
                        mixes.getDate("upload_date"),
                        mixes.getString("path"),
                        mixes.getString("session_name"),
                        mixes.getString("band_name")
                ));
                mixesTable.setItems(mixesObservableList);
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public void provideClientConfiguration(ObservableList<Mix> mixesObservableList,
                                           TableView<Mix> mixesTable, Integer userId) {
        try {
            DatabaseManager databaseManager = DatabaseManager.getInstance();
            mixesObservableList.clear();

            String query = "SELECT Mixes.id, " +
                    "Mixes.filename, " +
                    "Mixes.upload_date, " +
                    "Mixes.path, " +
                    "Session.session_name, " +
                    "Session.band_name " +
                    "FROM Mixes " +
                    "JOIN Sessions as Session " +
                    "ON Session.id = Mixes.Session_id " +
                    "WHERE Session.Clients_id = " + userId;
            ResultSet mixes = databaseManager.executeQuery(query);

            while (mixes.next()) {
                mixesObservableList.add(new Mix(
                        mixes.getInt("id"),
                        mixes.getString("filename"),
                        mixes.getDate("upload_date"),
                        mixes.getString("path"),
                        mixes.getString("session_name"),
                        mixes.getString("band_name")
                ));
                mixesTable.setItems(mixesObservableList);
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }
}
