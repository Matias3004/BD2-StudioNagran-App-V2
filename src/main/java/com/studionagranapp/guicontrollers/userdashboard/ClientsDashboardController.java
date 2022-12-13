package com.studionagranapp.guicontrollers.userdashboard;

import com.studionagranapp.helpers.errorhandling.AlertManager;
import com.studionagranapp.helpers.contentloaders.SceneCreator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ClientsDashboardController {

    @FXML
    private Button logoutButton;
    @FXML
    private BorderPane borderPane;
    @FXML
    private Label userInfo;
    private final AlertManager alertManager;

    public ClientsDashboardController() {
        alertManager = new AlertManager();
    }

    public void setUserInfo(String userInfo) {
        this.userInfo.setText(userInfo);
    }

    @FXML
    public void logout() {
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        stage.close();
        SceneCreator.createScene("gui/login-panel.fxml", 800, 600);
    }

    @FXML
    public void bookSession() {

    }
}
