package com.studionagranapp.helpers.errorhandling;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class AlertManager implements IAlertManager {

    @Override
    public void throwError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Błąd");
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    @Override
    public void throwInformation(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    @Override
    public boolean throwConfirmation(String message) {
        ButtonType yesButton = new ButtonType("Tak", ButtonBar.ButtonData.OK_DONE);
        ButtonType noButton = new ButtonType("Nie", ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, yesButton, noButton);
        alert.setTitle("Ostrzeżenie");
        alert.setHeaderText(null);

        return alert.showAndWait().orElse(yesButton) != noButton;
    }
}
