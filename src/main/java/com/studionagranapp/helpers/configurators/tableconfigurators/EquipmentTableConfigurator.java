package com.studionagranapp.helpers.configurators.tableconfigurators;

import com.studionagranapp.helpers.databaseconnection.DatabaseManager;
import com.studionagranapp.helpers.models.Equipment;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EquipmentTableConfigurator {

    public void provideFullConfiguration(ObservableList<Equipment> equipmentObservableList,
                                         TableView<Equipment> equipmentTable) {
        try {
            DatabaseManager databaseManager = DatabaseManager.getInstance();
            equipmentObservableList.clear();

            String query =  "SELECT * FROM Equipment";
            ResultSet equipment = databaseManager.executeQuery(query);

            while (equipment.next()) {
                equipmentObservableList.add(new Equipment(
                        equipment.getInt("id"),
                        equipment.getString("name"),
                        equipment.getString("type"),
                        equipment.getInt("quantity"),
                        equipment.getBoolean("backline")
                ));
                equipmentTable.setItems(equipmentObservableList);
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public void provideClientConfiguration(ObservableList<Equipment> equipmentObservableList,
                                           TableView<Equipment> equipmentTable) {
        try {
            DatabaseManager databaseManager = DatabaseManager.getInstance();
            equipmentObservableList.clear();

            String query = "SELECT * FROM Equipment WHERE backline = 1";
            ResultSet equipment = databaseManager.executeQuery(query);

            while (equipment.next()) {
                equipmentObservableList.add(new Equipment(
                        equipment.getInt("id"),
                        equipment.getString("name"),
                        equipment.getString("type"),
                        equipment.getInt("quantity"),
                        equipment.getBoolean("backline")
                ));
                equipmentTable.setItems(equipmentObservableList);
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }
}
