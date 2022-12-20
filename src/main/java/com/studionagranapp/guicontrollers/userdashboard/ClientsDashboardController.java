package com.studionagranapp.guicontrollers.userdashboard;

import com.studionagranapp.helpers.configurators.tableconfigurators.EquipmentTableConfigurator;
import com.studionagranapp.helpers.databaseconnection.DatabaseManager;
import com.studionagranapp.helpers.errorhandling.AlertManager;
import com.studionagranapp.helpers.contentloaders.SceneCreator;
import com.studionagranapp.helpers.models.Equipment;
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
import java.util.ResourceBundle;

public class ClientsDashboardController implements Initializable {

    @FXML
    private Button logoutButton;
    @FXML
    private BorderPane borderPane;
    @FXML
    private Label userInfo;
    @FXML
    TableView<Equipment> equipmentTable;
    @FXML
    TableColumn<Equipment, String> eqNameColumn;
    @FXML
    TableColumn<Equipment, Integer> eqTypeColumn;

    private DatabaseManager databaseManager;
    private EquipmentTableConfigurator equipmentTableConfigurator;
    private final ObservableList<Equipment> equipmentObservableList = FXCollections.observableArrayList();
    private final AlertManager alertManager;

    public ClientsDashboardController() {
        databaseManager = DatabaseManager.getInstance();
        equipmentTableConfigurator = new EquipmentTableConfigurator();
        alertManager = new AlertManager();
    }

    public void setUserInfo(String userInfo) {
        this.userInfo.setText(userInfo);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        equipmentTableConfigurator.provideClientConfiguration(equipmentObservableList, equipmentTable);
        initData();
    }

    @FXML
    public void logout() {
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        stage.close();
        SceneCreator.createScene("gui/login-panel.fxml", 800, 600);
    }

    @FXML
    public void bookSession() {

    }

    @FXML
    private void refresh() {
        equipmentTableConfigurator.provideClientConfiguration(equipmentObservableList, equipmentTable);
        initData();
    }

    private void initData() {
        eqNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        eqTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        equipmentTable.setItems(equipmentObservableList);

        FilteredList<Equipment> filteredData = new FilteredList<>(equipmentObservableList, b -> true);

        SortedList<Equipment> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(equipmentTable.comparatorProperty());

        equipmentTable.setItems(sortedData);
    }
}
