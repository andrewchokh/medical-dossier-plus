package com.andrewchokh.medicaldossierplus.Controllers.Client;

import com.andrewchokh.medicaldossierplus.Models.Model;
import com.andrewchokh.medicaldossierplus.Enums.Windows;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CodeVerificationController implements Initializable {

    public TextField codeField;
    public Button verifyButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        verifyButton.setOnAction(ActionEvent -> verifyCode());
    }

    private void verifyCode() {
        Stage stage = (Stage) verifyButton.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
        Model.getInstance().getViewFactory().showScene(Windows.CLIENT_PARENT.getPath(), null);
    }
}
