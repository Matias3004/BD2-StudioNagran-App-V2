package com.studionagranapp.guicontrollers.userdashboard.client;

import com.studionagranapp.helpers.configurators.tableconfigurators.EquipmentTableConfigurator;
import com.studionagranapp.helpers.contentloaders.SceneCreator;
import com.studionagranapp.helpers.databaseconnection.DatabaseManager;
import com.studionagranapp.helpers.databaseconnection.DatabaseResponse;
import com.studionagranapp.helpers.errorhandling.AlertManager;
import com.studionagranapp.helpers.models.Equipment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class NewClientsDashboardController implements Initializable {

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

    @FXML
    private TableView<Equipment> equipmentTable;
    @FXML
    private TableColumn<Equipment, String> eqNameColumn;
    @FXML
    private TableColumn<Equipment, String> eqTypeColumn;

    private final ObservableList<Equipment> equipmentObservableList = FXCollections.observableArrayList();

    private final DatabaseManager databaseManager;
    private final EquipmentTableConfigurator equipmentTableConfigurator;
    private final AlertManager alertManager;

    public NewClientsDashboardController() {
        databaseManager = DatabaseManager.getInstance();
        equipmentTableConfigurator = new EquipmentTableConfigurator();
        alertManager = new AlertManager();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        equipmentTableConfigurator.provideClientConfiguration(equipmentObservableList, equipmentTable);
        initEquipmentData();
    }

    @FXML
    private void goBack() {
        Stage stage = (Stage) returnButton.getScene().getWindow();
        stage.close();
        SceneCreator.createScene("gui/login-panel.fxml", 800, 600);
    }

    @FXML
    private void registerUser() {
        if (isDataFieldsBlank()) {
            DatabaseResponse newClientResult = databaseManager
                    .insertClient(nameField.getText(),
                            surnameField.getText(),
                            usernameField.getText(),
                            passwordField.getText(),
                            "CLIENT",
                            emailField.getText(),
                            phoneField.getText());
            if (newClientResult == DatabaseResponse.SUCCESS) {
                alertManager.throwInformation("Sesja zarezerwowana pomyslnie!");
                goBack();
            } else
                alertManager.throwError("Istnieje już użytkownik o wybranej nazwie!");
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

    private void initEquipmentData() {
        eqNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        eqTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        equipmentTable.setItems(equipmentObservableList);

        FilteredList<Equipment> filteredData = new FilteredList<>(equipmentObservableList, b -> true);

        SortedList<Equipment> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(equipmentTable.comparatorProperty());

        equipmentTable.setItems(sortedData);
    }
}
