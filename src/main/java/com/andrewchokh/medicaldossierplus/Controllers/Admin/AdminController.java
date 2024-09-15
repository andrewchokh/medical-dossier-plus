package com.andrewchokh.medicaldossierplus.Controllers.Admin;

import com.andrewchokh.medicaldossierplus.Enums.Windows;
import com.andrewchokh.medicaldossierplus.Models.Model;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

public class AdminController implements Initializable {

    public BorderPane adminParent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getViewFactory().getSideMenuItem().addListener((observableValue, oldVal, newVal) -> {
            try {
                switch (newVal) {
                    case "Diary" ->
                        adminParent.setCenter(Model.getInstance().getViewFactory().getView(
                            Windows.ADMIN_DIARY.getPath()));
                    case "Records" ->
                        adminParent.setCenter(Model.getInstance().getViewFactory().getView(
                            Windows.ADMIN_RECORDS.getPath()));
                    default -> adminParent.setCenter(Model.getInstance().getViewFactory().getView(
                        Windows.ADMIN_DASHBOARD.getPath()));
                }
            } catch (NullPointerException e) {
                // Ignored because of thread exception duplicates.
            }
        });
    }
}
