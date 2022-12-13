module com.example.studionagranapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.studionagranapp to javafx.fxml;
    opens com.studionagranapp.guicontrollers.login;
    exports com.studionagranapp;
    // opens com.studionagranapp.guicontrollers.userdashboard;
    // opens com.studionagranapp.guicontrollers.userdashboard to javafx.fxml;
}