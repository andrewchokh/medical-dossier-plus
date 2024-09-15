package com.andrewchokh.medicaldossierplus.Controllers;

import com.andrewchokh.medicaldossierplus.Enums.AccountTypes;
import com.andrewchokh.medicaldossierplus.Models.Model;
import com.andrewchokh.medicaldossierplus.Enums.Windows;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SignupController implements Initializable {
    public ChoiceBox accountTypeChoiceBox;
    public TextField firstNameField;
    public TextField lastNameField;
    public TextField loginField;
    public TextField emailField;
    public TextField passwordField;
    public TextField repeatPasswordField;
    public Button signUpButton;
    public Button logInButton;
    public Text errorText;

    private final ObservableList<String> accountTypes = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logInButton.setOnAction(ActionEvent -> logIn());
        signUpButton.setOnAction(ActionEvent -> signUp());
        loadAccountTypesData();
    }

    private void signUp() {
        Stage stage = (Stage) logInButton.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);

        var accountTypeChoice = accountTypeChoiceBox.getValue();
        String scenePath = null;

        if (accountTypeChoice == accountTypes.get(0)) {
            scenePath = Windows.VERIFICATION_CODE.getPath();
        }
        else if (accountTypeChoice == accountTypes.get(1)) {
            scenePath = Windows.ADMIN_VERIFICATION_CODE.getPath();
        }

        if (scenePath != null) {
            Model.getInstance().getViewFactory().showScene(scenePath, null);
        }
    }

    private void logIn() {
        Stage stage = (Stage) logInButton.getScene().getWindow();
        Model.getInstance().getViewFactory().showScene(Windows.LOGIN.getPath(), stage);
    }

    private void loadAccountTypesData() {
        accountTypes.removeAll();
        accountTypes.addAll(AccountTypes.REGULAR.toString(), AccountTypes.ADMIN.toString());
        accountTypeChoiceBox.getItems().addAll(accountTypes);
    }
}
