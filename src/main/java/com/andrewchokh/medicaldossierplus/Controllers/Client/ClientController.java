package com.andrewchokh.medicaldossierplus.Controllers.Client;

import com.andrewchokh.medicaldossierplus.Enums.SceneVariations;
import com.andrewchokh.medicaldossierplus.Enums.Windows;
import com.andrewchokh.medicaldossierplus.Models.Model;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

public class ClientController implements Initializable {

    public BorderPane clientParent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getViewFactory().getSceneVariation().addListener((observableValue, oldVal, newVal) -> {
            String windowPath = Windows.CLIENT_DASHBOARD.getPath();

            if (Objects.equals(newVal, SceneVariations.DIARY.toString())) {
                windowPath = Windows.CLIENT_DIARY.getPath();
            }
            else if (Objects.equals(newVal, SceneVariations.RECORDS.toString())) {
                windowPath = Windows.CLIENT_RECORDS.getPath();
            }

            clientParent.setCenter(Model.getInstance().getViewFactory().getView(windowPath));
        });
    }
}
