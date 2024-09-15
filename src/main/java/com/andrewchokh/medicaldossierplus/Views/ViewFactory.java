package com.andrewchokh.medicaldossierplus.Views;

import java.io.IOException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ViewFactory {
    private final StringProperty sideMenuItem;

    public ViewFactory() {
        this.sideMenuItem = new SimpleStringProperty("");
    }

    public StringProperty getSideMenuItem() {
        return sideMenuItem;
    }

    public void showScene(String scenePath, Stage stage) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(scenePath));
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
