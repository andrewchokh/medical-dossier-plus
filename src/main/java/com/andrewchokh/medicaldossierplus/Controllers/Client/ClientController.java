package com.andrewchokh.medicaldossierplus.Controllers.Client;

import com.andrewchokh.medicaldossierplus.Enums.Windows;
import com.andrewchokh.medicaldossierplus.Models.Model;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

public class ClientController implements Initializable {

    public BorderPane clientParent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getViewFactory().getSideMenuItem().addListener((observableValue, oldVal, newVal) -> {
            try {
                switch (newVal) {
                    case "Diary" ->
                        clientParent.setCenter(Model.getInstance().getViewFactory().getView(
                            Windows.CLIENT_DIARY.getPath()));
                    case "Records" ->
                        clientParent.setCenter(Model.getInstance().getViewFactory().getView(
                            Windows.CLIENT_RECORDS.getPath()));
                    default -> clientParent.setCenter(Model.getInstance().getViewFactory().getView(
                        Windows.CLIENT_DASHBOARD.getPath()));
                }
            } catch (NullPointerException e) {
                // Ignored because of thread exception duplicates.
            }
        });
    }
}
