package com.andrewchokh.medicaldossierplus.Controllers.Client;

import com.andrewchokh.medicaldossierplus.Enums.SceneVariations;
import com.andrewchokh.medicaldossierplus.Models.Model;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class ClientMenuController implements Initializable {

    public Button homeButton;
    public Button diaryButton;
    public Button recordsButton;
    public Button reportButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        onSideMenuButton();
    }

    private void onSideMenuButton() {
        homeButton.setOnAction(this::onNavigationButton);
        diaryButton.setOnAction(this::onNavigationButton);
        recordsButton.setOnAction(this::onNavigationButton);

        reportButton.setOnAction(this::onReport);
    }

    private void onNavigationButton(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        String buttonId = button.getId();

        switch (buttonId) {
            case "homeButton" -> Model.getInstance().getViewFactory().getSceneVariation().set(
                SceneVariations.HOME.toString());
            case "diaryButton" -> Model.getInstance().getViewFactory().getSceneVariation().set(
                SceneVariations.DIARY.toString());
            case "recordsButton" -> Model.getInstance().getViewFactory().getSceneVariation().set(
                SceneVariations.RECORDS.toString());
            default -> Model.getInstance().getViewFactory().getSceneVariation().set("");
        }
    }

    private void onReport(ActionEvent actionEvent) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                URI uri = new URI("https://github.com/andrewchokh/medical-dossier-plus/issues");
                desktop.browse(uri);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }
}
