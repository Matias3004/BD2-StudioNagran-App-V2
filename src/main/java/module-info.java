module com.example.studionagranapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;
    requires mysql.connector.java;

    opens com.studionagranapp to javafx.fxml;
    opens com.studionagranapp.guicontrollers.login;
    opens com.studionagranapp.helpers.models;
    exports com.studionagranapp;
    opens com.studionagranapp.helpers.databaseconnection;
    opens com.studionagranapp.helpers.loginvalidation;
    opens com.studionagranapp.helpers.query;
    exports com.studionagranapp.helpers.contentloaders;
    opens com.studionagranapp.helpers.contentloaders to javafx.fxml;
    exports com.studionagranapp.guicontrollers.userdashboard.client;
    opens com.studionagranapp.guicontrollers.userdashboard.client to javafx.fxml;
    exports com.studionagranapp.guicontrollers.userdashboard.engineer;
    opens com.studionagranapp.guicontrollers.userdashboard.engineer to javafx.fxml;
    exports com.studionagranapp.guicontrollers.userdashboard.owner;
    opens com.studionagranapp.guicontrollers.userdashboard.owner to javafx.fxml;
}