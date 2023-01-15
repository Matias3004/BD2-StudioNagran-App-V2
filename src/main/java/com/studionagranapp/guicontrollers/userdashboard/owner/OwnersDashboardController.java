package com.studionagranapp.guicontrollers.userdashboard.owner;

import com.studionagranapp.guicontrollers.userdashboard.ModifySessionController;
import com.studionagranapp.helpers.configurators.tableconfigurators.ClientsTableConfigurator;
import com.studionagranapp.helpers.configurators.tableconfigurators.EngineersTableConfigurator;
import com.studionagranapp.helpers.configurators.tableconfigurators.EquipmentTableConfigurator;
import com.studionagranapp.helpers.configurators.tableconfigurators.SessionsTableConfigurator;
import com.studionagranapp.helpers.contentloaders.SceneCreator;
import com.studionagranapp.helpers.databaseconnection.DatabaseManager;
import com.studionagranapp.helpers.databaseconnection.DatabaseResponse;
import com.studionagranapp.helpers.errorhandling.AlertManager;
import com.studionagranapp.helpers.models.Equipment;

import com.studionagranapp.helpers.models.Session;
import com.studionagranapp.helpers.models.User;
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
import java.sql.Date;
import java.util.ResourceBundle;

public class OwnersDashboardController implements Initializable {

    private Integer userId;

    @FXML
    private Label userInfo;
    @FXML
    private Button logoutButton;

    @FXML
    TableView<Session> sessionsTable;
    @FXML
    TableColumn<Session, String> seshNameColumn;
    @FXML
    TableColumn<Session, String> seshClientColumn;
    @FXML
    TableColumn<Session, String> seshBandColumn;
    @FXML
    TableColumn<Session, String> seshEngineerColumn;
    @FXML
    TableColumn<Session, Date> seshBeginColumn;
    @FXML
    TableColumn<Session, Date> seshEndColumn;
    @FXML
    TableColumn<Session, Integer> seshDurationColumn;

    @FXML
    TableView<User> clientsTable;
    @FXML
    TableColumn<User, String> clientFullNameColumn;
    @FXML
    TableColumn<User, String> clientEmailColumn;
    @FXML
    TableColumn<User, String> clientPhoneColumn;

    @FXML
    TableView<User> engineersTable;
    @FXML
    TableColumn<User, String> engineerFullNameColumn;
    @FXML
    TableColumn<User, String> engineerEmailColumn;
    @FXML
    TableColumn<User, String> engineerPhoneColumn;

    @FXML
    TableView<Equipment> equipmentTable;
    @FXML
    TableColumn<Equipment, String> eqNameColumn;
    @FXML
    TableColumn<Equipment, String> eqTypeColumn;
    @FXML
    TableColumn<Equipment, Integer> eqQuantityColumn;

    private final ObservableList<Session> sessionsObservableList = FXCollections.observableArrayList();
    private final ObservableList<User> clientsObservableList = FXCollections.observableArrayList();
    private final ObservableList<User> engineersObservableList = FXCollections.observableArrayList();
    private final ObservableList<Equipment> equipmentObservableList = FXCollections.observableArrayList();

    private final DatabaseManager databaseManager;
    private final SessionsTableConfigurator sessionsTableConfigurator;
    private final ClientsTableConfigurator clientsTableConfigurator;
    private final EngineersTableConfigurator engineersTableConfigurator;
    private final EquipmentTableConfigurator equipmentTableConfigurator;
    private final AlertManager alertManager;

    public OwnersDashboardController() {
        databaseManager = DatabaseManager.getInstance();
        sessionsTableConfigurator = new SessionsTableConfigurator();
        clientsTableConfigurator = new ClientsTableConfigurator();
        engineersTableConfigurator = new EngineersTableConfigurator();
        equipmentTableConfigurator = new EquipmentTableConfigurator();
        alertManager = new AlertManager();
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo.setText(userInfo);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sessionsTableConfigurator.provideFullConfiguration(sessionsObservableList, sessionsTable);
        clientsTableConfigurator.provideConfiguration(clientsObservableList, clientsTable);
        engineersTableConfigurator.provideConfiguration(engineersObservableList, engineersTable);
        equipmentTableConfigurator.provideFullConfiguration(equipmentObservableList, equipmentTable);
        initSessionsData();
        initClientsData();
        initEngineersData();
        initEquipmentData();
    }

    @FXML
    private void refresh() {
        sessionsTableConfigurator.provideFullConfiguration(sessionsObservableList, sessionsTable);
        clientsTableConfigurator.provideConfiguration(clientsObservableList, clientsTable);
        engineersTableConfigurator.provideConfiguration(engineersObservableList, engineersTable);
        equipmentTableConfigurator.provideFullConfiguration(equipmentObservableList, equipmentTable);
        initSessionsData();
        initClientsData();
        initEngineersData();
        initEquipmentData();
    }

    @FXML
    private void logout() {
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        stage.close();
        SceneCreator.createScene("gui/login-panel.fxml", 800, 600);
    }

    @FXML
    public void scheduleSession() {
    }

    @FXML
    public void modifySession() {
        try {
            Session session = sessionsTable.getSelectionModel().getSelectedItem();
            Integer sessionID = session.getId();

            ModifySessionController modifySessionController = (ModifySessionController)
                    SceneCreator.createScene("gui/modify-session-window.fxml", 240, 150);
            assert modifySessionController != null;
            modifySessionController.setAlertManager(alertManager);
            modifySessionController.setSessionID(sessionID);
        } catch (Exception e) {
            alertManager.throwError("Nie wybrano żadnej sesji z listy!");
        }
    }

    @FXML
    public void cancelSession() {
        try {
            Session session = sessionsTable.getSelectionModel().getSelectedItem();
            DatabaseResponse result = databaseManager.delete(session);
            if (result == DatabaseResponse.ERROR)
                alertManager.throwError("Wystąpił błąd podczas usuwania sesji");
            else if (result == DatabaseResponse.SUCCESS) {
                alertManager.throwConfirmation("Pomyslnie odwołano sesję!");
                refresh();
            }
        } catch (Exception e) {
            alertManager.throwError("Nie wybrano żadnej sesji z listy!");
        }
    }

    @FXML
    public void addEngineer() {
    }

    @FXML
    public void deleteEngineer() {
    }

    @FXML
    public void addEquipment() {
    }

    @FXML
    public void deleteEquipment() {
        try {
            Equipment equipment = equipmentTable.getSelectionModel().getSelectedItem();

            DatabaseResponse result = databaseManager.delete(equipment);
            if (result == DatabaseResponse.ERROR)
                alertManager.throwError("Wystąpił błąd podczas usuwania danych z bazy.");
            else if (result == DatabaseResponse.SUCCESS) {
                alertManager.throwConfirmation("Sprzęt usunięty pomyslnie!");
                refresh();
            }
        } catch (Exception e) {
            alertManager.throwError("Nie wybrano żadnego sprzętu z listy!");
        }
    }

    @FXML
    public void modifyEqQuantity() {
        try {
            Equipment equipment = equipmentTable.getSelectionModel().getSelectedItem();
            Integer equipmentID = equipment.getId();

            ModifyQuantityController modifyQuantityController = (ModifyQuantityController)
                SceneCreator.createScene("gui/modify-quantity-window.fxml", 200, 100);
            assert modifyQuantityController != null;
            modifyQuantityController.setAlertManager(alertManager);
            modifyQuantityController.setEquipmentID(equipmentID);
        } catch (Exception e) {
            alertManager.throwError("Nie wybrano żadnego przedmiotu z listy!");
        }
    }

    private void initSessionsData() {
        seshNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        seshClientColumn.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        seshBandColumn.setCellValueFactory(new PropertyValueFactory<>("bandName"));
        seshEngineerColumn.setCellValueFactory(new PropertyValueFactory<>("engineerName"));
        seshBeginColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        seshEndColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        seshDurationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));

        sessionsTable.setItems(sessionsObservableList);

        FilteredList<Session> filteredData = new FilteredList<>(sessionsObservableList, b -> true);

        SortedList<Session> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(sessionsTable.comparatorProperty());

        sessionsTable.setItems(sortedData);
    }

    private void initClientsData() {
        clientFullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        clientEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        clientPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        clientsTable.setItems(clientsObservableList);

        FilteredList<User> filteredData = new FilteredList<>(clientsObservableList, b -> true);

        SortedList<User> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(clientsTable.comparatorProperty());

        clientsTable.setItems(sortedData);
    }

    private void initEngineersData() {
        engineerFullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        engineerEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        engineerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        engineersTable.setItems(engineersObservableList);

        FilteredList<User> filteredData = new FilteredList<>(engineersObservableList, b -> true);

        SortedList<User> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(engineersTable.comparatorProperty());

        engineersTable.setItems(sortedData);
    }

    private void initEquipmentData() {
        eqNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        eqTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        eqQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        equipmentTable.setItems(equipmentObservableList);

        FilteredList<Equipment> filteredData = new FilteredList<>(equipmentObservableList, b -> true);

        SortedList<Equipment> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(equipmentTable.comparatorProperty());

        equipmentTable.setItems(sortedData);
    }
}
