package com.studionagranapp.guicontrollers.userdashboard.engineer;

import com.studionagranapp.guicontrollers.userdashboard.ModifySessionController;
import com.studionagranapp.helpers.configurators.choiceboxconfigurators.SessionsChoiceBoxConfigurator;
import com.studionagranapp.helpers.configurators.tableconfigurators.EquipmentTableConfigurator;
import com.studionagranapp.helpers.configurators.tableconfigurators.MixesTableConfigurator;
import com.studionagranapp.helpers.configurators.tableconfigurators.SessionsTableConfigurator;
import com.studionagranapp.helpers.contentloaders.SceneCreator;
import com.studionagranapp.helpers.databaseconnection.DatabaseManager;
import com.studionagranapp.helpers.databaseconnection.DatabaseResponse;
import com.studionagranapp.helpers.errorhandling.AlertManager;
import com.studionagranapp.helpers.models.Equipment;
import com.studionagranapp.helpers.models.Mix;
import com.studionagranapp.helpers.models.MixNote;
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
    private TableColumn<Equipment, String> eqTypeColumn;
    @FXML
    private TableColumn<Equipment, Integer> eqQuantityColumn;

    private final ObservableList<Session> sessionsObservableList = FXCollections.observableArrayList();
    private final ObservableList<Mix> mixesObservableList = FXCollections.observableArrayList();
    private final ObservableList<MixNote> mixNotesObservableList = FXCollections.observableArrayList();
    private final ObservableList<Equipment> equipmentObservableList = FXCollections.observableArrayList();
    private final DatabaseManager databaseManager;
    private final SessionsTableConfigurator sessionsTableConfigurator;
    private final SessionsChoiceBoxConfigurator sessionsChoiceBoxConfigurator;
    private final MixesTableConfigurator mixesTableConfigurator;
    private final EquipmentTableConfigurator equipmentTableConfigurator;
    private final AlertManager alertManager;

    public EngineersDashboardController() {
        databaseManager = DatabaseManager.getInstance();
        sessionsTableConfigurator = new SessionsTableConfigurator();
        sessionsChoiceBoxConfigurator = new SessionsChoiceBoxConfigurator();
        mixesTableConfigurator = new MixesTableConfigurator();
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
        mixesTableConfigurator.provideEngineerConfiguration(mixesObservableList, mixesTable, userId);
        equipmentTableConfigurator.provideFullConfiguration(equipmentObservableList, equipmentTable);
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
    private void modifySession() {
        try {
            Session session = mySessionsTable.getSelectionModel().getSelectedItem();
            Integer sessionID = session.getId();

            if (ChronoUnit.DAYS.between(LocalDate.now(), session.getStartDate().toLocalDate()) < 3) {
                alertManager.throwError("Nie możesz zmienić terminu sesji na mniej niż 3 dni przed zaplanowaną datą!");

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
            if (ChronoUnit.DAYS.between(LocalDate.now(), session.getStartDate().toLocalDate()) < 2) {
                alertManager.throwError("Nie możesz anulować sesji na mniej niż 2 dni przed zaplanowaną datą!");

                return;
            }

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
    private void addMix() {
        Dialog<Pair<String, String>> addMixDialog = new Dialog<>();
        addMixDialog.setTitle("Dodawanie miksu do sesji");
        addMixDialog.setHeaderText("Dodaj miks");

        ButtonType loginButtonType = new ButtonType("Dodaj", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Powrót", ButtonBar.ButtonData.CANCEL_CLOSE);
        addMixDialog.getDialogPane().getButtonTypes().addAll(loginButtonType, cancelButtonType);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField filename = new TextField();
        filename.setPromptText("Nazwa pliku");
        TextField filePath = new TextField();
        filePath.setPromptText("Ścieżka do pliku");
        ChoiceBox<String> sessionChoiceBox = new ChoiceBox<>();
        initSessionsChoiceBox(sessionChoiceBox);

        grid.add(new Label("Nazwa pliku:"), 0, 0);
        grid.add(filename, 1, 0);
        grid.add(new Label("Ścieżka do pliku:"), 0, 1);
        grid.add(filePath, 1, 1);
        grid.add(new Label("Sesja:"), 0, 2);
        grid.add(sessionChoiceBox, 1, 2);

        addMixDialog.getDialogPane().setContent(grid);

        Platform.runLater(filename::requestFocus);

        Optional<Pair<String, String>> result = addMixDialog.showAndWait();

        if (result.isPresent() && !filename.getText().isBlank() &&
                !filePath.getText().isBlank() &&
                !(sessionChoiceBox.getSelectionModel().getSelectedItem() == null)) {
            DatabaseResponse newMixResult = databaseManager
                    .insertMix(filename.getText(),
                            filePath.getText(),
                            sessionChoiceBox.getSelectionModel().getSelectedItem());
            if (newMixResult == DatabaseResponse.SUCCESS) {
                alertManager.throwConfirmation("Miks dodany pomyslnie!");
                refresh();
            }
            else
                alertManager.throwError("Błąd zapisu danych do bazy!");
        } else {
            alertManager.throwError("Sprawdź wprowadzone dane!");
            addMix();
        }
    }

    @FXML
    private void refresh() {
        sessionsTableConfigurator.provideEngineerConfiguration(sessionsObservableList, mySessionsTable, userId);
        mixesTableConfigurator.provideEngineerConfiguration(mixesObservableList, mixesTable, userId);
        equipmentTableConfigurator.provideFullConfiguration(equipmentObservableList, equipmentTable);
        initSessionsData();
        initMixesData();
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

    private void initSessionsChoiceBox(ChoiceBox<String> sessionsChoiceBox) {
        String query = "SELECT * FROM Sessions WHERE Engineer_id = " + this.userId;
        sessionsChoiceBoxConfigurator.initValues(query, sessionsChoiceBox);
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
