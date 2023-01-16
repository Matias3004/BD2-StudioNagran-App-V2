package com.studionagranapp.helpers.contentloaders;

import com.studionagranapp.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneCreator {

    public static Object createScene(String GUIFormFilepath, final int WIDTH, final int HEIGHT) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(GUIFormFilepath));
            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load(), WIDTH, HEIGHT);
            stage.setScene(scene);
            stage.show();

            return fxmlLoader.getController();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

        return null;
    }
}
