package com.studionagranapp.guicontrollers.userdashboard;

import com.studionagranapp.helpers.configurators.datepickerconfigurators.DatePickerConfigurator;
import com.studionagranapp.helpers.databaseconnection.DatabaseManager;
import com.studionagranapp.helpers.databaseconnection.DatabaseResponse;
import com.studionagranapp.helpers.errorhandling.AlertManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ModifySessionController implements Initializable {

    private Integer sessionID;

    @FXML
    private Button returnButton;
    @FXML
    private DatePicker newStartDateField;
    @FXML
    private DatePicker newEndDateField;

    private final DatabaseManager databaseManager;
    private final DatePickerConfigurator datePickerConfigurator;
    private AlertManager alertManager;

    public ModifySessionController() {
        databaseManager = DatabaseManager.getInstance();
        datePickerConfigurator = new DatePickerConfigurator();
    }

    public void setSessionID(Integer sessionID) {
        this.sessionID = sessionID;
    }

    public void setAlertManager(AlertManager alertManager) {
        this.alertManager = alertManager;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initDatePickers();
    }

    @FXML
    private void close() {
        Stage stage = (Stage) returnButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void changeDate() {
        if (isDateFieldsBlank()) {
            DatabaseResponse result = databaseManager
                    .updateSessionDate(this.sessionID,
                            newStartDateField.getValue(),
                            newEndDateField.getValue());
            if (result == DatabaseResponse.SUCCESS) {
                alertManager.throwInformation("Pomyslnie zmieniono sesję!");
                close();
            } else if (result == DatabaseResponse.SESSION_DATE_OCCUPIED)
                alertManager.throwError("Wybrany termin jest zajęty!");
            else
                alertManager.throwError("Wystąpił błąd podczas modyfikacji sesji");
        } else {
            alertManager.throwError("Podaj nowe daty!");
        }
    }

    private boolean isDateFieldsBlank() {
        return newStartDateField.getValue() != null || newEndDateField.getValue() != null;
    }

    private void initDatePickers() {
        String query = "SELECT start_date, end_date FROM Sessions";
        datePickerConfigurator.provideConfiguration(query, newStartDateField);
        datePickerConfigurator.provideConfiguration(query, newEndDateField);
    }
}
