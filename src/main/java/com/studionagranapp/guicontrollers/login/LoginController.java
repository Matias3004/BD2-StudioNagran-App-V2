package com.studionagranapp.guicontrollers.login;

import com.studionagranapp.guicontrollers.userdashboard.client.ClientsDashboardController;
import com.studionagranapp.guicontrollers.userdashboard.engineer.EngineersDashboardController;
import com.studionagranapp.guicontrollers.userdashboard.client.NewClientsDashboardController;
import com.studionagranapp.guicontrollers.userdashboard.owner.OwnersDashboardController;
import com.studionagranapp.helpers.loginvalidation.LoginValidation;
import com.studionagranapp.helpers.loginvalidation.LoginValidator;
import com.studionagranapp.helpers.loginvalidation.UserInfoProvider;
import com.studionagranapp.helpers.loginvalidation.ValidationResult;

import com.studionagranapp.helpers.contentloaders.SceneCreator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private Label statementField;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button exitButton;

    @FXML
    private void login() {
        if (!usernameField.getText().isBlank() && !passwordField.getText().isBlank()) {
            LoginValidation loginValidator = new LoginValidator(usernameField.getText(), passwordField.getText());
            ValidationResult result = loginValidator.validate();

            if (result == ValidationResult.AUTHORIZATION_OBTAINED) {
                statementField.setTextFill(Color.GREEN);
                statementField.setText("Zalogowano pomyślnie!");

                UserInfoProvider userInfoProvider = new UserInfoProvider(usernameField.getText(), passwordField.getText());
                switch (userInfoProvider.getRole()) {
                    case "OWNER" -> {
                        OwnersDashboardController ownersDashboardController = (OwnersDashboardController)
                                SceneCreator.createScene("gui/owner-dashboard.fxml", 800, 600);
                        assert ownersDashboardController != null;
                        ownersDashboardController.setUserInfo(userInfoProvider.getName());
                        quit();
                    }
                    case "ENGINEER" -> {
                        EngineersDashboardController engineersDashboardController = (EngineersDashboardController)
                                SceneCreator.createScene("gui/engineer-dashboard.fxml", 800, 600);
                        assert engineersDashboardController != null;
                        engineersDashboardController.setUserId(userInfoProvider.getId());
                        engineersDashboardController.setUserInfo(userInfoProvider.getName());
                        quit();
                    }
                    case "CLIENT" -> {
                        ClientsDashboardController clientsDashboardController = (ClientsDashboardController)
                                SceneCreator.createScene("gui/client-dashboard.fxml", 800, 600);
                        assert clientsDashboardController != null;
                        clientsDashboardController.setUserId(userInfoProvider.getId());
                        clientsDashboardController.setUserInfo(userInfoProvider.getName());
                        quit();
                    }
                    default -> {}
                }
            } else if (result == ValidationResult.INVALID_LOGIN) {
                statementField.setTextFill(Color.RED);
                statementField.setText("Nieprawidłowe dane logowania. Spróbuj ponownie");
            }
        } else {
            statementField.setTextFill(Color.RED);
            statementField.setText("Wprowadź nazwę użytkownika oraz hasło");
        }
    }

    @FXML
    private void newClient() {
        NewClientsDashboardController newClientsDashboardController = (NewClientsDashboardController)
                SceneCreator.createScene("gui/new-client-dashboard.fxml", 800, 600);
        assert newClientsDashboardController != null;
        quit();
    }

    @FXML
    private void quit() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
}
