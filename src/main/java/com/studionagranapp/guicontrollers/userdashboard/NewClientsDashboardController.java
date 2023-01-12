package com.studionagranapp.guicontrollers.userdashboard;

import com.studionagranapp.helpers.configurators.choiceboxconfigurators.EngineersChoiceBoxConfigurator;
import com.studionagranapp.helpers.configurators.tableconfigurators.EquipmentTableConfigurator;
import com.studionagranapp.helpers.contentloaders.SceneCreator;
import com.studionagranapp.helpers.databaseconnection.DatabaseManager;
import com.studionagranapp.helpers.errorhandling.AlertManager;
import com.studionagranapp.helpers.models.Equipment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Filter;

public class NewClientsDashboardController implements Initializable {

    @FXML
    private Button returnButton;
    @FXML
    private BorderPane borderPane;
    @FXML
    private ChoiceBox<String> engineersChoiceBox;

    @FXML
    private TableView<Equipment> equipmentTable;
    @FXML
    private TableColumn<Equipment, String> eqNameColumn;
    @FXML
    private TableColumn<Equipment, String> eqTypeColumn;

    private final ObservableList<Equipment> equipmentObservableList = FXCollections.observableArrayList();

    private final DatabaseManager databaseManager;
    private final EngineersChoiceBoxConfigurator engineersChoiceBoxConfigurator;
    private final EquipmentTableConfigurator equipmentTableConfigurator;
    private final AlertManager alertManager;

    public NewClientsDashboardController() {
        databaseManager = DatabaseManager.getInstance();
        engineersChoiceBoxConfigurator = new EngineersChoiceBoxConfigurator();
        equipmentTableConfigurator = new EquipmentTableConfigurator();
        alertManager = new AlertManager();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initEngineersChoiceBox();
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
    private void bookSession() {
        System.out.println("Sesja zarezerwowana!");
    }

    private void initEngineersChoiceBox() {
        String query = "SELECT * FROM User_accounts WHERE role = 'ENGINEER'";
        engineersChoiceBoxConfigurator.initValues(query, engineersChoiceBox);
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
