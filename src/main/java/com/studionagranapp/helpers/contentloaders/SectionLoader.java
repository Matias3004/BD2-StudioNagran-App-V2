package com.studionagranapp.helpers.contentloaders;

import com.studionagranapp.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

public class SectionLoader {

    public static Object load(String GUIFormFilepath, BorderPane borderPane) {
        Parent root = null;
        FXMLLoader fxmlLoader = null;
        try {
            fxmlLoader = new FXMLLoader(Main.class.getResource(GUIFormFilepath));
            root = fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        borderPane.setCenter(root);

        assert fxmlLoader != null;
        return fxmlLoader.getController();
    }
}
