package com.studionagranapp.guicontrollers.userdashboard.client;

import com.studionagranapp.helpers.configurators.choiceboxconfigurators.EngineersChoiceBoxConfigurator;
import com.studionagranapp.helpers.configurators.datepickerconfigurators.DatePickerConfigurator;
import com.studionagranapp.helpers.databaseconnection.DatabaseManager;
import com.studionagranapp.helpers.databaseconnection.DatabaseResponse;
import com.studionagranapp.helpers.errorhandling.AlertManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class NewSessionDashboardController implements Initializable {

    private Integer userID;

    @FXML
    private Button returnButton;
    @FXML
    private TextField sessionNameField;
    @FXML
    private TextField bandNameField;
    @FXML
    private ChoiceBox<String> engineersChoiceBox;
    @FXML
    private DatePicker startDateField;
    @FXML
    private DatePicker endDateField;

    private final DatabaseManager databaseManager;
    private final EngineersChoiceBoxConfigurator engineersChoiceBoxConfigurator;
    private final DatePickerConfigurator datePickerConfigurator;
    private AlertManager alertManager;

    public NewSessionDashboardController() {
        databaseManager = DatabaseManager.getInstance();
        engineersChoiceBoxConfigurator = new EngineersChoiceBoxConfigurator();
        datePickerConfigurator = new DatePickerConfigurator();
        alertManager = new AlertManager();
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initEngineersChoiceBox();
        initDatePickers();
    }

    @FXML
    private void goBack() {
        Stage stage = (Stage) returnButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void scheduleSession() {
        if (isDataFieldsBlank()) {
            DatabaseResponse newSessionResult = databaseManager
                    .insertSession(sessionNameField.getText(),
                            bandNameField.getText(),
                            startDateField.getValue(),
                            endDateField.getValue(),
                            this.userID,
                            engineersChoiceBox.getValue());
            if (newSessionResult == DatabaseResponse.SUCCESS) {
                alertManager.throwInformation("Sesja zarezerwowana pomyslnie!");
                goBack();
            } else if (newSessionResult == DatabaseResponse.SESSION_DATE_OCCUPIED)
                alertManager.throwError("Wybrany termin jest zajęty!");
            else
                alertManager.throwError("Istnieje już sesja o tej samej nazwie!");
        } else {
            alertManager.throwError("Błąd zapisu danych do bazy!");
        }
    }

    private boolean isDataFieldsBlank() {
        try {
            return !sessionNameField.getText().isBlank() &&
                    !bandNameField.getText().isBlank() &&
                    !engineersChoiceBox.getValue().isBlank();
        } catch (Exception e) {
            return false;
        }
    }

    public void setAlertManager(AlertManager alertManager) {
        this.alertManager = alertManager;
    }

    private void initEngineersChoiceBox() {
        String query = "SELECT * FROM User_accounts WHERE role = 'ENGINEER'";
        engineersChoiceBoxConfigurator.initValues(query, engineersChoiceBox);
    }

    private void initDatePickers() {
        String query = "SELECT start_date, end_date FROM Sessions";
        datePickerConfigurator.provideConfiguration(query, startDateField);
        datePickerConfigurator.provideConfiguration(query, endDateField);
    }
}
