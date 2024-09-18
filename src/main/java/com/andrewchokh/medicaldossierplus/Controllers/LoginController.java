package com.andrewchokh.medicaldossierplus.Controllers;

import static com.andrewchokh.medicaldossierplus.App.currentUser;
import static com.andrewchokh.medicaldossierplus.Utils.AuthorizationUtil.changeLabelText;

import com.andrewchokh.medicaldossierplus.Database.SQLite;
import com.andrewchokh.medicaldossierplus.Enums.AccountTypes;
import com.andrewchokh.medicaldossierplus.Enums.SceneVariations;
import com.andrewchokh.medicaldossierplus.Enums.Windows;
import com.andrewchokh.medicaldossierplus.Models.Model;
import com.andrewchokh.medicaldossierplus.Types.Builders.UserBuilder;
import com.andrewchokh.medicaldossierplus.Utils.AuthorizationUtil;
import java.net.URL;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginController implements Initializable {
    public TextField loginField;
    public TextField passwordField;
    public Button logInButton;
    public Button signUpButton;
    public Label errorLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logInButton.setOnAction(actionEvent -> onLogIn());
        signUpButton.setOnAction(actionEvent -> onSignUp());
    }

    private void onLogIn() {
        String login = loginField.getText();
        String password = AuthorizationUtil.hashPassword(passwordField.getText());

        var query = SQLite.executeQuery(
            String.format("SELECT * FROM Users WHERE email = '%s'", login)
        );

        if (query.isEmpty()) {
            showAuthorizationFailMessage();
            return;
        }

        HashMap<String, Object> userData = (HashMap<String, Object>) query.get(0);

        if (login.equals(userData.get("email")) && password.equals(userData.get("password"))) {
            int accountType = (int) userData.get("account_type");
            Windows windowPath = null;

            currentUser = new UserBuilder()
                .id((int) userData.get("id"))
                .firstName((String) userData.get("first_name"))
                .lastName((String) userData.get("last_name"))
                .email((String) userData.get("email"))
                .password((String) userData.get("password"))
                .accountType((int) userData.get("account_type"))
                .build();

            if (accountType == AccountTypes.REGULAR.getValue()) {
                windowPath = Windows.CLIENT_PARENT;
            }
            else if (accountType == AccountTypes.ADMIN.getValue()) {
                windowPath = Windows.ADMIN_PARENT;
            }

            if (windowPath != null) {
                Stage stage = (Stage) errorLabel.getScene().getWindow();
                Model.getInstance().getViewFactory().closeStage(stage);
                Model.getInstance().getViewFactory().showScene(windowPath, null);
            }
        }
        else {
            showAuthorizationFailMessage();
        }
    }

    private void onSignUp() {
        Stage stage = (Stage) errorLabel.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
        Model.getInstance().getViewFactory().showScene(Windows.SIGNUP, null);
    }

    private void showAuthorizationFailMessage() {
        changeLabelText(errorLabel, "Invalid login or password!");
    }
}
