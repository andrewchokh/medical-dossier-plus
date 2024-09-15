package com.andrewchokh.medicaldossierplus.Controllers.Client;

import com.andrewchokh.medicaldossierplus.Models.Model;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

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
    }

    private void onNavigationButton(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        String buttonId = button.getId();

        System.out.println(buttonId);

        switch (buttonId) {
            case "homeButton" -> Model.getInstance().getViewFactory().getSideMenuItem().set("Home");
            case "diaryButton" -> Model.getInstance().getViewFactory().getSideMenuItem().set("Diary");
            case "recordsButton" -> Model.getInstance().getViewFactory().getSideMenuItem().set("Records");
            default -> Model.getInstance().getViewFactory().getSideMenuItem().set("");
        }
    }
}
