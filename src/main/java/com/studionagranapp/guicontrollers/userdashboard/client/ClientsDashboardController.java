package com.studionagranapp.guicontrollers.userdashboard.client;

import com.studionagranapp.guicontrollers.userdashboard.ModifySessionController;
import com.studionagranapp.helpers.configurators.tableconfigurators.EquipmentTableConfigurator;
import com.studionagranapp.helpers.configurators.tableconfigurators.SessionsTableConfigurator;
import com.studionagranapp.helpers.databaseconnection.DatabaseManager;
import com.studionagranapp.helpers.databaseconnection.DatabaseResponse;
import com.studionagranapp.helpers.errorhandling.AlertManager;
import com.studionagranapp.helpers.contentloaders.SceneCreator;
import com.studionagranapp.helpers.models.Equipment;
import com.studionagranapp.helpers.models.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

public class ClientsDashboardController implements Initializable {

    private Integer userId;

    @FXML
    private Button logoutButton;
    @FXML
    private BorderPane borderPane;
    @FXML
    private Label userInfo;

    @FXML
    TableView<Session> mySessionsTable;
    @FXML
    TableColumn<Session, String> seshNameColumn;
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
    TableColumn<Equipment, Integer> eqTypeColumn;

    private final ObservableList<Session> sessionsObservableList = FXCollections.observableArrayList();
    private final ObservableList<Equipment> equipmentObservableList = FXCollections.observableArrayList();

    private final DatabaseManager databaseManager;
    private final SessionsTableConfigurator sessionsTableConfigurator;
    private final EquipmentTableConfigurator equipmentTableConfigurator;
    private final AlertManager alertManager;

    public ClientsDashboardController() {
        databaseManager = DatabaseManager.getInstance();
        sessionsTableConfigurator = new SessionsTableConfigurator();
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
        sessionsTableConfigurator.provideClientConfiguration(sessionsObservableList, mySessionsTable, userId);
        equipmentTableConfigurator.provideClientConfiguration(equipmentObservableList, equipmentTable);
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
    private void bookSession() {
        NewSessionDashboardController newSessionDashboardController = (NewSessionDashboardController)
                SceneCreator.createScene("gui/new-session-dashboard.fxml", 800, 600);
        assert newSessionDashboardController != null;
        newSessionDashboardController.setUserID(userId);
        newSessionDashboardController.setAlertManager(alertManager);
    }

    @FXML
    public void modifySession() {
        try {
            Session session = mySessionsTable.getSelectionModel().getSelectedItem();
            Integer sessionID = session.getId();

            if (ChronoUnit.DAYS.between(LocalDate.now(), session.getStartDate().toLocalDate()) < 7) {
                alertManager.throwError("Nie możesz zmienić terminu sesji na mniej niż tydzień przed zaplanowanym terminem!");

                return;
            }

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
    private void cancelSession() {
        try {
            Session session = mySessionsTable.getSelectionModel().getSelectedItem();
            if (ChronoUnit.DAYS.between(LocalDate.now(), session.getStartDate().toLocalDate()) < 7) {
                alertManager.throwError("Nie możesz odwołać sesji na mniej niż tydzień przed zaplanowanym terminem!");

                return;
            }
            DatabaseResponse result = databaseManager.delete(session);
            if (result == DatabaseResponse.ERROR)
                alertManager.throwError("Wystąpił błąd podczas usuwania sesji.");
            else if (result == DatabaseResponse.SUCCESS) {
                alertManager.throwConfirmation("Pomyslnie odwołano sesję!");
                refresh();
            }
        } catch (Exception e) {
            alertManager.throwError("Nie wybrano żadnej sesji z listy!");
        }
    }

    @FXML
    private void refresh() {
        sessionsTableConfigurator.provideClientConfiguration(sessionsObservableList, mySessionsTable, userId);
        equipmentTableConfigurator.provideClientConfiguration(equipmentObservableList, equipmentTable);
        initSessionsData();
        initEquipmentData();
    }

    private void initSessionsData() {
        seshNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        seshBandColumn.setCellValueFactory(new PropertyValueFactory<>("bandName"));
        seshEngineerColumn.setCellValueFactory(new PropertyValueFactory<>("engineerName"));
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

        equipmentTable.setItems(equipmentObservableList);

        FilteredList<Equipment> filteredData = new FilteredList<>(equipmentObservableList, b -> true);

        SortedList<Equipment> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(equipmentTable.comparatorProperty());

        equipmentTable.setItems(sortedData);
    }
}
