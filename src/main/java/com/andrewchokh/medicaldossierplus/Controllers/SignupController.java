package com.andrewchokh.medicaldossierplus.Controllers;

import com.andrewchokh.medicaldossierplus.Database.SQLite;
import com.andrewchokh.medicaldossierplus.Enums.AccountTypes;
import com.andrewchokh.medicaldossierplus.Models.Model;
import com.andrewchokh.medicaldossierplus.Enums.Windows;
import com.andrewchokh.medicaldossierplus.Utils.AuthorizationUtil;
import com.andrewchokh.medicaldossierplus.Utils.DatabaseUtil;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignupController implements Initializable {
    public ChoiceBox accountTypeChoiceBox;
    public TextField firstNameField;
    public TextField lastNameField;
    public TextField emailField;
    public TextField passwordField;
    public TextField repeatPasswordField;
    public Button signUpButton;
    public Button logInButton;
    public Label errorLabel;

    private final ObservableList<String> accountTypes = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logInButton.setOnAction(ActionEvent -> logIn());
        signUpButton.setOnAction(ActionEvent -> signUp());
        loadAccountTypesData();
    }

    private void signUp() {
        String accountTypeName;
        String firstName;
        String lastName;
        String email;
        String password;
        String repeatPassword;

        try {
            accountTypeName = accountTypeChoiceBox.getValue().toString();
            firstName = firstNameField.getText();
            lastName = lastNameField.getText();
            email = emailField.getText();
            password = passwordField.getText();
            repeatPassword = repeatPasswordField.getText();
        } catch (Exception e) {
            AuthorizationUtil.changeLabelText(errorLabel, "Invalid data!");
            return;
        }

        if (isAccountExist(email)) {
            AuthorizationUtil.changeLabelText(errorLabel, "Account with the same email already exists!");
            return;
        }

        if (!validateData(accountTypeName, firstName, lastName, email, password, repeatPassword)) {
            AuthorizationUtil.changeLabelText(errorLabel, "Invalid data!");
            return;
        }

        int accountType = 0;

        if (Objects.equals(accountTypeName, AccountTypes.REGULAR.toString())) {
            accountType = 0;
        }
        else if (Objects.equals(accountTypeName, AccountTypes.ADMIN.toString())) {
            accountType = 1;
        }

        int id = DatabaseUtil.generateRandomID();
        String hashPassword = AuthorizationUtil.hashPassword(password);

        SQLite.executeUpdate(String.format(
            """
            INSERT INTO Users (id, first_name, last_name, email, password, account_type) 
            VALUES (%d, '%s', '%s', '%s', '%s', %d)
            """,
            id, firstName, lastName, email, hashPassword, accountType
        ));

        logIn();
    }

    private void logIn() {
        Stage stage = (Stage) errorLabel.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
        Model.getInstance().getViewFactory().showScene(Windows.LOGIN, null);
    }

    private void loadAccountTypesData() {
        accountTypes.removeAll();
        accountTypes.addAll(AccountTypes.REGULAR.toString(), AccountTypes.ADMIN.toString());
        accountTypeChoiceBox.getItems().addAll(accountTypes);
    }

    private Boolean validateData(String accountTypeName, String firstName, String lastName,
        String email, String password, String repeatPassword) {
        // Account type validation
        if (accountTypeName.isEmpty()) {
            return false;
        }

        // Name validation
        if (!(firstName.length() >= 2 && firstName.length() <= 24) ||
            !(lastName.length() >= 2 && lastName.length() <= 24)) {
            return false;
        }

        // Email validation
        if (!(email.length() >= 10 && email.length() <= 34) &&
            email.contains("@") && email.contains("gmail.com")) {
            return false;
        }

        // Password validation
        if ((!Objects.equals(password, repeatPassword))) {
            return false;
        }
        if (!(password.length() >= 8 && password.length() <= 24) ||
            !(repeatPassword.length() >= 8 && repeatPassword.length() <= 24)) {
            return false;
        }

        return true;
    }

    private Boolean isAccountExist(String email) {
        List<Object> query = SQLite.executeQuery(
            "SELECT email FROM Users WHERE email = '%s'"
                .formatted(email)
        );

        return query.isEmpty();
    }
}
