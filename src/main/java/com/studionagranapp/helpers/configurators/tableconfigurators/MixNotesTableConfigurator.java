package com.studionagranapp.helpers.configurators.tableconfigurators;

import com.studionagranapp.helpers.databaseconnection.DatabaseManager;
import com.studionagranapp.helpers.models.MixNote;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MixNotesTableConfigurator {

    public void provideEngineerConfiguration(ObservableList<MixNote> mixNotesObservableList,
                                             TableView<MixNote> mixNotesTable, Integer id) {
        try {
            DatabaseManager databaseManager = DatabaseManager.getInstance();
            mixNotesObservableList.clear();

            String query = "SELECT Mix_notes.id, " +
                    "Mix_notes.upload_date, " +
                    "Mix_notes.description, " +
                    "Mix.filename, " +
                    "Session.session_name, " +
                    "Session.band_name " +
                    "FROM Mix_notes " +
                    "JOIN Mixes as Mix " +
                    "ON Mix.id = Mix_notes.Mix_id " +
                    "JOIN Sessions as Session " +
                    "ON Session.id = Mix.Session_id " +
                    "WHERE Session.Engineer_id = " + id;
            ResultSet mixNotes = databaseManager.executeQuery(query);

            while (mixNotes.next()) {
                mixNotesObservableList.add(new MixNote(
                        mixNotes.getInt("id"),
                        mixNotes.getDate("upload_date"),
                        mixNotes.getString("description"),
                        mixNotes.getString("filename"),
                        mixNotes.getString("session_name"),
                        mixNotes.getString("band_name")
                ));
                mixNotesTable.setItems(mixNotesObservableList);
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

//    public void provideClientConfiguration(ObservableList<MixNote> mixNotesObservableList,
//                                             TableView<MixNote> mixNotesTable, Integer id) {
//        try {
//            DatabaseManager databaseManager = DatabaseManager.getInstance();
//            mixNotesObservableList.clear();
//
//            String query = "SELECT Mix_notes.id, " +
//                    "Mix_notes.upload_date, " +
//                    "Mix_notes.description, " +
//                    "Mix.filename, " +
//                    "Session.name as session_name, " +
//                    "Session.band_name " +
//                    "FROM Mixes " +
//                    "JOIN Mixes as Mix " +
//                    "ON Mix.id = Mix_notes.Mix_id " +
//                    "JOIN Sessions as Session " +
//                    "ON Session.id = Mix.id " +
//                    "WHERE Session.Client_id = " + id;
//            ResultSet mixNotes = databaseManager.executeQuery(query);
//
//            while (mixNotes.next()) {
//                mixNotesObservableList.add(new MixNote(
//                        mixNotes.getInt("id"),
//                        mixNotes.getDate("upload_date"),
//                        mixNotes.getString("description"),
//                        mixNotes.getString("filename"),
//                        mixNotes.getString("session_name"),
//                        mixNotes.getString("band_name")
//                ));
//                mixNotesTable.setItems(mixNotesObservableList);
//            }
//        } catch (SQLException throwable) {
//            throwable.printStackTrace();
//        }
//    }
}
