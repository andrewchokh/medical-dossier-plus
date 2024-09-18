package com.andrewchokh.medicaldossierplus.Controllers.Admin;

import com.andrewchokh.medicaldossierplus.App;
import com.andrewchokh.medicaldossierplus.Enums.SceneVariations;
import com.andrewchokh.medicaldossierplus.Enums.Windows;
import com.andrewchokh.medicaldossierplus.Models.Model;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class AdminController implements Initializable {

    public BorderPane adminParent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getViewFactory().getSceneVariation().addListener((observableValue, oldVal, newVal) -> {
            String windowPath = Windows.ADMIN_DASHBOARD.getPath();

            if (Objects.equals(newVal, SceneVariations.DIARY.toString())) {
                windowPath = Windows.ADMIN_DIARY.getPath();
            }
            else if (Objects.equals(newVal, SceneVariations.RECORDS.toString())) {
                windowPath = Windows.ADMIN_RECORDS.getPath();
            }

            adminParent.setCenter(Model.getInstance().getViewFactory().getView(windowPath));
        });
    }
}
