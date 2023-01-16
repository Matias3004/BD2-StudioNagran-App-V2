package com.studionagranapp.guicontrollers.userdashboard.owner;

import com.studionagranapp.helpers.databaseconnection.DatabaseManager;
import com.studionagranapp.helpers.databaseconnection.DatabaseResponse;
import com.studionagranapp.helpers.errorhandling.AlertManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class NewEngineerDashboardController {

    @FXML
    private Button returnButton;
    @FXML
    private TextField nameField;
    @FXML
    private TextField surnameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    private final DatabaseManager databaseManager;
    private final AlertManager alertManager;

    public NewEngineerDashboardController() {
        databaseManager = DatabaseManager.getInstance();
        alertManager = new AlertManager();
    }

    @FXML
    private void goBack() {
        Stage stage = (Stage) returnButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void registerUser() {
        if (isDataFieldsBlank()) {
            DatabaseResponse newClientResult = databaseManager
                    .insertClient(nameField.getText(),
                            surnameField.getText(),
                            usernameField.getText(),
                            passwordField.getText(),
                            "ENGINEER",
                            emailField.getText(),
                            phoneField.getText());
            if (newClientResult == DatabaseResponse.SUCCESS) {
                alertManager.throwInformation("Realizator dodany pomyslnie!");
                goBack();
            } else
                alertManager.throwError("Istnieje już użytkownik o tej samej nazwie!");
        } else
            alertManager.throwError("Błąd zapisu danych do bazy!");
    }

    private boolean isDataFieldsBlank() {
        try {
            return !nameField.getText().isBlank() &&
                    !surnameField.getText().isBlank() &&
                    !emailField.getText().isBlank() &&
                    !phoneField.getText().isBlank() &&
                    !usernameField.getText().isBlank() &&
                    !passwordField.getText().isBlank();
        } catch (Exception e) {
            return false;
        }
    }
}
