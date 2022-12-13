module com.example.studionagranapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;
    requires mysql.connector.java;

    opens com.studionagranapp to javafx.fxml;
    opens com.studionagranapp.guicontrollers.login;
    opens com.studionagranapp.helpers.models;
    exports com.studionagranapp;
    exports com.studionagranapp.guicontrollers.userdashboard;
    opens com.studionagranapp.guicontrollers.userdashboard to javafx.fxml;
    opens com.studionagranapp.helpers.databaseconnection;
    opens com.studionagranapp.helpers.loginvalidation;
    opens com.studionagranapp.helpers.query;
    exports com.studionagranapp.helpers.contentloaders;
    opens com.studionagranapp.helpers.contentloaders to javafx.fxml;
}