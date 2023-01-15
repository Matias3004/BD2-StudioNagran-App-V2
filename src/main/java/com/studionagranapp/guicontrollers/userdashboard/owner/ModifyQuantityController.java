package com.studionagranapp.guicontrollers.userdashboard.owner;

import com.studionagranapp.helpers.databaseconnection.DatabaseManager;
import com.studionagranapp.helpers.databaseconnection.DatabaseResponse;
import com.studionagranapp.helpers.errorhandling.AlertManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ModifyQuantityController {

    private Integer equipmentID;

    @FXML
    private Button returnButton;
    @FXML
    private TextField newQuantityField;

    private final DatabaseManager databaseManager;
    private AlertManager alertManager;

    public ModifyQuantityController() {
        databaseManager = DatabaseManager.getInstance();
    }

    public void setEquipmentID(Integer equipmentID) {
        this.equipmentID = equipmentID;
    }

    public void setAlertManager(AlertManager alertManager) {
        this.alertManager = alertManager;
    }

    @FXML
    private void close() {
        Stage stage = (Stage) returnButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void modifyQuantity() {
        if (isQuantityFieldBlank()) {
            DatabaseResponse result = databaseManager
                    .updateEquipmentQuantity(this.equipmentID, newQuantityField.getText());
            if (result == DatabaseResponse.SUCCESS) {
                alertManager.throwConfirmation("Zaaktualizowano ilosć!");
                close();
            } else
                alertManager.throwError("Wystąpił błąd podczas zmiany ilosci sprzetu");
        } else {
            alertManager.throwError("Podaj nową ilosć!");
        }
    }

    private boolean isQuantityFieldBlank() {
        try {
            return !newQuantityField.getText().isBlank();
        } catch (Exception e) {
            return false;
        }
    }
}
