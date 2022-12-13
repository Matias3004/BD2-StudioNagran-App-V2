package com.studionagranapp.helpers.errorhandling;

import javafx.scene.control.Alert;

public class AlertManager implements IAlertManager {

    @Override
    public void throwError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    @Override
    public void throwConfirmation(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
