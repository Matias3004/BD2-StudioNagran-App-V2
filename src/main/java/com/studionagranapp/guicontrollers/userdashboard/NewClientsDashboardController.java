package com.studionagranapp.guicontrollers.userdashboard;

import com.studionagranapp.helpers.contentloaders.SceneCreator;
import com.studionagranapp.helpers.errorhandling.AlertManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class NewClientsDashboardController {

    @FXML
    private Button returnButton;
    @FXML
    private BorderPane borderPane;
    private final AlertManager alertManager;

    public NewClientsDashboardController() {
        alertManager = new AlertManager();
    }

    @FXML
    private void goBack() {
        Stage stage = (Stage) returnButton.getScene().getWindow();
        stage.close();
        SceneCreator.createScene("gui/login-panel.fxml", 800, 600);
    }

    @FXML
    private void bookSession() {
        System.out.println("Sesja zarezerwowana!");
    }
}
