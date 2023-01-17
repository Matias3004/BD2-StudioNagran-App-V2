package com.studionagranapp.guicontrollers.userdashboard.client;

import com.studionagranapp.guicontrollers.userdashboard.ModifySessionController;
import com.studionagranapp.helpers.configurators.tableconfigurators.EquipmentTableConfigurator;
import com.studionagranapp.helpers.configurators.tableconfigurators.MixesTableConfigurator;
import com.studionagranapp.helpers.configurators.tableconfigurators.SessionsTableConfigurator;
import com.studionagranapp.helpers.databaseconnection.DatabaseManager;
import com.studionagranapp.helpers.databaseconnection.DatabaseResponse;
import com.studionagranapp.helpers.errorhandling.AlertManager;
import com.studionagranapp.helpers.contentloaders.SceneCreator;
import com.studionagranapp.helpers.models.Equipment;
import com.studionagranapp.helpers.models.Mix;
import com.studionagranapp.helpers.models.Session;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.ResourceBundle;

public class ClientsDashboardController implements Initializable {

    private Integer userId;

    @FXML
    private Button logoutButton;
    @FXML
    private Label userInfo;

    @FXML
    private TableView<Session> mySessionsTable;
    @FXML
    private TableColumn<Session, String> seshNameColumn;
    @FXML
    private TableColumn<Session, String> seshBandColumn;
    @FXML
    private TableColumn<Session, String> seshEngineerColumn;
    @FXML
    private TableColumn<Session, Date> seshBeginColumn;
    @FXML
    private TableColumn<Session, Date> seshEndColumn;
    @FXML
    private TableColumn<Session, Integer> seshDurationColumn;

    @FXML
    private TableView<Mix> mixesTable;
    @FXML
    private TableColumn<Mix, String> mixNameColumn;
    @FXML
    private TableColumn<Mix, Date> mixDateColumn;
    @FXML
    private TableColumn<Mix, String> mixSessionColumn;

    @FXML
    private TableView<Equipment> equipmentTable;
    @FXML
    private TableColumn<Equipment, String> eqNameColumn;
    @FXML
    private TableColumn<Equipment, Integer> eqTypeColumn;

    private final ObservableList<Session> sessionsObservableList = FXCollections.observableArrayList();
    private final ObservableList<Mix> mixesObservableList = FXCollections.observableArrayList();
    private final ObservableList<Equipment> equipmentObservableList = FXCollections.observableArrayList();

    private final DatabaseManager databaseManager;
    private final SessionsTableConfigurator sessionsTableConfigurator;
    private final MixesTableConfigurator mixesTableConfigurator;
    private final EquipmentTableConfigurator equipmentTableConfigurator;
    private final AlertManager alertManager;

    public ClientsDashboardController() {
        databaseManager = DatabaseManager.getInstance();
        sessionsTableConfigurator = new SessionsTableConfigurator();
        mixesTableConfigurator = new MixesTableConfigurator();
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
        mixesTableConfigurator.provideClientConfiguration(mixesObservableList, mixesTable, userId);
        equipmentTableConfigurator.provideClientConfiguration(equipmentObservableList, equipmentTable);
        initSessionsData();
        initMixesData();
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
                alertManager.throwError(
                        "Nie możesz zmienić terminu sesji na mniej niż tydzień przed zaplanowanym terminem!");

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
            if (session == null)
                throw new Exception();

            if (ChronoUnit.DAYS.between(LocalDate.now(), session.getStartDate().toLocalDate()) < 7) {
                alertManager.throwError("Nie możesz odwołać sesji na mniej niż tydzień przed zaplanowanym terminem!");

                return;
            }

            boolean confirmation = alertManager
                    .throwConfirmation("Czy na pewno chcesz odwołać wybraną sesję?");
            if (!confirmation)
                return;

            DatabaseResponse result = databaseManager.delete(session);
            if (result == DatabaseResponse.ERROR)
                alertManager.throwError("Wystąpił błąd podczas usuwania sesji.");
            else if (result == DatabaseResponse.SUCCESS) {
                alertManager.throwInformation("Pomyslnie odwołano sesję!");
                refresh();
            }
        } catch (Exception e) {
            alertManager.throwError("Nie wybrano żadnej sesji z listy!");
        }
    }

    @FXML
    private void addMixNote() {
        try {
            Mix mix = mixesTable.getSelectionModel().getSelectedItem();
            if (mix == null)
                throw new Exception();

            Dialog<ButtonType> addMixDialog = new Dialog<>();
            addMixDialog.setTitle("Dodawanie uwagi do miksu");
            addMixDialog.setHeaderText("Dodaj uwagę");

            ButtonType loginButtonType = new ButtonType("Dodaj", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButtonType = new ButtonType("Powrót", ButtonBar.ButtonData.CANCEL_CLOSE);
            addMixDialog.getDialogPane().getButtonTypes().addAll(loginButtonType, cancelButtonType);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextArea description = new TextArea();
            description.setPromptText("Uwaga");

            grid.add(new Label("Tresć uwagi:"), 0, 0);
            grid.add(description, 0, 1);

            addMixDialog.getDialogPane().setContent(grid);

            Platform.runLater(description::requestFocus);

            Optional<ButtonType> result = addMixDialog.showAndWait();

            if (result.isPresent() && !description.getText().isBlank()) {
                DatabaseResponse newMixResult = databaseManager
                        .insertMixNote(description.getText(), mix.getId());
                if (newMixResult == DatabaseResponse.SUCCESS) {
                    alertManager.throwInformation("Uwaga do miksu dodana pomyslnie!");
                    refresh();
                } else
                    alertManager.throwError("Błąd zapisu danych do bazy!");
            } else if (result.isPresent() && result.get() == cancelButtonType)
                return;
            else {
                alertManager.throwError("Sprawdź wprowadzone dane!");
                addMixNote();
            }
        } catch (Exception e) {
            alertManager.throwError("Nie wybrano żadnego miksu z listy!");
        }
    }

    @FXML
    private void refresh() {
        sessionsTableConfigurator.provideClientConfiguration(sessionsObservableList, mySessionsTable, userId);
        mixesTableConfigurator.provideClientConfiguration(mixesObservableList, mixesTable, userId);
        equipmentTableConfigurator.provideClientConfiguration(equipmentObservableList, equipmentTable);
        initSessionsData();
        initMixesData();
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

    private void initMixesData() {
        mixNameColumn.setCellValueFactory(new PropertyValueFactory<>("filename"));
        mixDateColumn.setCellValueFactory(new PropertyValueFactory<>("uploadDate"));
        mixSessionColumn.setCellValueFactory(new PropertyValueFactory<>("sessionName"));

        mixesTable.setItems(mixesObservableList);

        FilteredList<Mix> filteredData = new FilteredList<>(mixesObservableList, b -> true);

        SortedList<Mix> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(mixesTable.comparatorProperty());

        mixesTable.setItems(sortedData);
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
