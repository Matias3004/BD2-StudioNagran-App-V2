package com.studionagranapp.helpers.configurators.tableconfigurators;

import com.studionagranapp.helpers.databaseconnection.DatabaseManager;
import com.studionagranapp.helpers.models.Session;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SessionsTableConfigurator {

    public void provideFullConfiguration(ObservableList<Session> sessionsObservableList,
                                         TableView<Session> sessionsTable) {
        try {
            DatabaseManager databaseManager = DatabaseManager.getInstance();
            sessionsObservableList.clear();

            String query = "SELECT Sessions.id, " +
                    "Sessions.name, " +
                    "Sessions.band_name, " +
                    "Sessions.start_date, " +
                    "Sessions.end_date, " +
                    "Sessions.duration, " +
                    "Client.first_name as client_first_name, " +
                    "Client.last_name as client_last_name, " +
                    "Engineer.first_name as eng_first_name, " +
                    "Engineer.last_name as eng_last_name " +
                    "FROM Studio_nagran.Sessions " +
                    "JOIN Studio_nagran.User_accounts as Client " +
                    "ON Client.account_id = Sessions.Clients_id " +
                    "JOIN Studio_nagran.User_accounts as Engineer " +
                    "ON Engineer.account_id = Sessions.Engineer_id";
            ResultSet sessions = databaseManager.executeQuery(query);

            while (sessions.next()) {
                sessionsObservableList.add(new Session(
                        sessions.getInt("id"),
                        sessions.getString("name"),
                        sessions.getString("band_name"),
                        sessions.getDate("start_date"),
                        sessions.getDate("end_date"),
                        sessions.getInt("duration"),
                        sessions.getString("client_first_name")
                                + " " + sessions.getString("client_last_name"),
                        sessions.getString("eng_first_name")
                                + " " + sessions.getString("eng_last_name")
                ));
                sessionsTable.setItems(sessionsObservableList);
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public void provideEngineerConfiguration(ObservableList<Session> sessionsObservableList,
                                             TableView<Session> sessionsTable, Integer id) {
        try {
            DatabaseManager databaseManager = DatabaseManager.getInstance();
            sessionsObservableList.clear();

            String query = "SELECT Sessions.id, " +
                    "Sessions.name, " +
                    "Sessions.band_name, " +
                    "Sessions.start_date, " +
                    "Sessions.end_date, " +
                    "Sessions.duration, " +
                    "Client.first_name as client_first_name, " +
                    "Client.last_name as client_last_name, " +
                    "Engineer.first_name as eng_first_name, " +
                    "Engineer.last_name as eng_last_name " +
                    "FROM Studio_nagran.Sessions " +
                    "JOIN Studio_nagran.User_accounts as Client " +
                    "ON Client.account_id = Sessions.Clients_id " +
                    "JOIN Studio_nagran.User_accounts as Engineer " +
                    "ON Engineer.account_id = Sessions.Engineer_id " +
                    "where Engineer_id = " + id;
            ResultSet sessions = databaseManager.executeQuery(query);

            while (sessions.next()) {
                sessionsObservableList.add(new Session(
                        sessions.getInt("id"),
                        sessions.getString("name"),
                        sessions.getString("band_name"),
                        sessions.getDate("start_date"),
                        sessions.getDate("end_date"),
                        sessions.getInt("duration"),
                        sessions.getString("client_first_name")
                            + " " + sessions.getString("client_last_name"),
                        sessions.getString("eng_first_name")
                            + " " + sessions.getString("eng_last_name")
                ));
                sessionsTable.setItems(sessionsObservableList);
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }
}
