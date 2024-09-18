package com.andrewchokh.medicaldossierplus.Views;

import com.andrewchokh.medicaldossierplus.Enums.Windows;
import java.io.IOException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ViewFactory {
    private final StringProperty sceneVariation;

    public ViewFactory() {
        this.sceneVariation = new SimpleStringProperty("");
    }

    public StringProperty getSceneVariation() {
        return sceneVariation;
    }

    public void showScene(Windows window, Stage stage) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(window.getPath()));
        Scene scene = null;

        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (stage == null) {
            stage = new Stage();
        }

        stage.setTitle("Medical Dossier +");

        stage.setScene(scene);
        stage.show();
    }

    public AnchorPane getView(String scenePath) {
        AnchorPane view = null;

        try {
            view = new FXMLLoader(getClass().getResource(scenePath)).load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return view;
    }

    public void closeStage(Stage stage) {
        stage.close();
    }
}
