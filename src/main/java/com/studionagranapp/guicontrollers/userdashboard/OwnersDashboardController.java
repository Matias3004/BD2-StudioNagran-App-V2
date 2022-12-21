package com.studionagranapp.guicontrollers.userdashboard;

import com.studionagranapp.helpers.configurators.tableconfigurators.EquipmentTableConfigurator;
import com.studionagranapp.helpers.configurators.tableconfigurators.SessionsTableConfigurator;
import com.studionagranapp.helpers.contentloaders.SceneCreator;
import com.studionagranapp.helpers.databaseconnection.DatabaseManager;
import com.studionagranapp.helpers.errorhandling.AlertManager;
import com.studionagranapp.helpers.models.Equipment;

import com.studionagranapp.helpers.models.Session;
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
import java.util.Date;
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
    TableView<Equipment> equipmentTable;
    @FXML
    TableColumn<Equipment, String> eqNameColumn;
    @FXML
    TableColumn<Equipment, String> eqTypeColumn;
    @FXML
    TableColumn<Equipment, Integer> eqQuantityColumn;

    private final ObservableList<Session> sessionsObservableList = FXCollections.observableArrayList();
    private final ObservableList<Equipment> equipmentObservableList = FXCollections.observableArrayList();
    private final DatabaseManager databaseManager;
    private final SessionsTableConfigurator sessionsTableConfigurator;
    private final EquipmentTableConfigurator equipmentTableConfigurator;
    private final AlertManager alertManager;

    public OwnersDashboardController() {
        databaseManager = DatabaseManager.getInstance();
        sessionsTableConfigurator = new SessionsTableConfigurator();
        equipmentTableConfigurator = new EquipmentTableConfigurator();
        alertManager = new AlertManager();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sessionsTableConfigurator.provideFullConfiguration(sessionsObservableList, sessionsTable);
        equipmentTableConfigurator.provideFullConfiguration(equipmentObservableList, equipmentTable);
        initSessionsData();
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
    }

    @FXML
    public void cancelSession() {
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo.setText(userInfo);
    }

    @FXML
    private void refresh() {
        sessionsTableConfigurator.provideFullConfiguration(sessionsObservableList, sessionsTable);
        equipmentTableConfigurator.provideFullConfiguration(equipmentObservableList, equipmentTable);
        initSessionsData();
        initEquipmentData();
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
