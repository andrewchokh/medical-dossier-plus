package com.andrewchokh.medicaldossierplus.Controllers;

import com.andrewchokh.medicaldossierplus.Models.Model;
import com.andrewchokh.medicaldossierplus.Enums.Windows;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginController implements Initializable {

    public Text loginText;
    public TextField loginField;
    public Text passwordText;
    public TextField passwordField;
    public Button logInButton;
    public Button signUpButton;
    public Text errorText;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logInButton.setOnAction(actionEvent -> onLogIn());
        signUpButton.setOnAction(actionEvent -> onSignUp());
    }

    private void onLogIn() {
        Stage stage = (Stage) logInButton.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
        Model.getInstance().getViewFactory().showScene(Windows.CLIENT_PARENT.getPath(), null);
    }

    private void onSignUp() {
        Stage stage = (Stage) logInButton.getScene().getWindow();
        Model.getInstance().getViewFactory().showScene(Windows.SIGNUP.getPath(), stage);
    }

    private void changeErrorText(String text) {
        errorText.setVisible(!text.isEmpty());
        errorText.setText(text);
    }
}
