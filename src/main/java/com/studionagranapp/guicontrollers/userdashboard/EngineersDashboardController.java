package com.studionagranapp.guicontrollers.userdashboard;

import com.studionagranapp.helpers.configurators.tableconfigurators.EquipmentTableConfigurator;
import com.studionagranapp.helpers.configurators.tableconfigurators.SessionsTableConfigurator;
import com.studionagranapp.helpers.contentloaders.SceneCreator;
import com.studionagranapp.helpers.databaseconnection.DatabaseManager;
import com.studionagranapp.helpers.errorhandling.AlertManager;
import com.studionagranapp.helpers.models.Equipment;
import com.studionagranapp.helpers.models.Mix;
import com.studionagranapp.helpers.models.MixNote;
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
import java.sql.Date;
import java.util.ResourceBundle;

public class EngineersDashboardController implements Initializable {

    private Integer userId;

    @FXML
    private Label userInfo;
    @FXML
    private Button logoutButton;
    @FXML
    private Button modifySessionButton;
    @FXML
    private Button cancelSessionButton;
    @FXML
    private Button addMixButton;

    @FXML
    private TableView<Session> mySessionsTable;
    @FXML
    private TableColumn<Session, String> seshNameColumn;
    @FXML
    private TableColumn<Session, String> seshClientColumn;
    @FXML
    private TableColumn<Session, String> seshBandColumn;
    @FXML
    private TableColumn<Session, Date> seshBeginColumn;
    @FXML
    private TableColumn<Session, Date> seshEndColumn;
    @FXML
    private TableColumn<Session, Integer>seshDurationColumn;

    @FXML
    private TableView<Equipment> equipmentTable;
    @FXML
    private TableColumn<Equipment, String> eqNameColumn;
    @FXML
    private TableColumn<Equipment, String> eqTypeColumn;
    @FXML
    private TableColumn<Equipment, Integer> eqQuantityColumn;

    private final ObservableList<Session> sessionsObservableList = FXCollections.observableArrayList();
    private final ObservableList<Mix> mixesObservableList = FXCollections.observableArrayList();
    private final ObservableList<MixNote> mixNotesObservableList = FXCollections.observableArrayList();
    private final ObservableList<Equipment> equipmentObservableList = FXCollections.observableArrayList();
    private final DatabaseManager databaseManager;
    private final SessionsTableConfigurator sessionsTableConfigurator;
    private final EquipmentTableConfigurator equipmentTableConfigurator;
    private final AlertManager alertManager;

    public EngineersDashboardController() {
        databaseManager = DatabaseManager.getInstance();
        sessionsTableConfigurator = new SessionsTableConfigurator();
        equipmentTableConfigurator = new EquipmentTableConfigurator();
        alertManager = new AlertManager();
    }

    public void setUserId(Integer id) {
        this.userId = id;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo.setText(userInfo);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sessionsTableConfigurator.provideEngineerConfiguration(sessionsObservableList, mySessionsTable, userId);
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
    private void modifySession() {

    }

    @FXML
    private void cancelSession() {

    }

    @FXML
    private void addMix() {

    }

    @FXML
    private void refresh() {
        sessionsTableConfigurator.provideEngineerConfiguration(sessionsObservableList, mySessionsTable, userId);
        equipmentTableConfigurator.provideFullConfiguration(equipmentObservableList, equipmentTable);
        initSessionsData();
        initEquipmentData();
    }

    private void initSessionsData() {
        seshNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        seshClientColumn.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        seshBandColumn.setCellValueFactory(new PropertyValueFactory<>("bandName"));
        seshBeginColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        seshEndColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        seshDurationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));

        mySessionsTable.setItems(sessionsObservableList);

        FilteredList<Session> filteredData = new FilteredList<>(sessionsObservableList, b -> true);

        SortedList<Session> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(mySessionsTable.comparatorProperty());

        mySessionsTable.setItems(sortedData);
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
