package com.andrewchokh.medicaldossierplus.Controllers.Admin;

import static com.andrewchokh.medicaldossierplus.Utils.UsersUtil.calculateCharacteristics;
import static com.andrewchokh.medicaldossierplus.Utils.UsersUtil.loadRegularUsers;
import static com.andrewchokh.medicaldossierplus.Utils.UsersUtil.updateRegularUserList;

import com.andrewchokh.medicaldossierplus.App;
import com.andrewchokh.medicaldossierplus.Database.SQLite;
import com.andrewchokh.medicaldossierplus.Types.User;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class AdminDashboardController implements Initializable {

    public Label nameLabel;
    public Button removeUserButton;
    public Label firstNameLabel;
    public Label lastNameLabel;
    public Label weightLabel;
    public Label pulseLabel;
    public Label emailLabel;
    public Label pressureLabel;
    public Label temperatureLabel;
    public ListView userList;

    private final List<User> users = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setNameLabel();

        removeUserButton.setOnAction(actionEvent -> removeUser());

        refreshUsers();

        userList.getSelectionModel().selectedItemProperty().addListener(this::switchUser);
    }

    private void setNameLabel() {
        nameLabel.setText(App.currentUser.getFirstName());
    }

    private void switchUser(ObservableValue<String> observableValue, Object o, Object o1) {
        int index = userList.getItems().indexOf(observableValue.getValue());

        if (index < 0) {
            firstNameLabel.setText("First name: -");
            lastNameLabel.setText("Last name: -");
            pressureLabel.setText("Blood pressure: -");
            emailLabel.setText("Email: -");
            temperatureLabel.setText("Body temperature: -");
            pulseLabel.setText("Pulse: -");
            weightLabel.setText("Weight: -");

            return;
        }

        User user = users.get(index);
        if (user == null) return;

        firstNameLabel.setText("First name: %s".formatted(user.getFirstName()));
        lastNameLabel.setText("Last name: %s".formatted(user.getLastName()));
        emailLabel.setText("Email: %s".formatted(user.getEmail()));

        calculateCharacteristics(user.getId(), weightLabel, pulseLabel, pressureLabel, temperatureLabel);
    }

    private void removeUser() {
        String name = (String) userList.getSelectionModel().selectedItemProperty().getValue();

        if (name == null) return;

        int index = userList.getItems().indexOf(name);

        SQLite.executeUpdate(
            "DELETE FROM Users WHERE id = %d"
                .formatted(users.get(index).getId())
        );

        userList.getItems().remove(index, index + 1);

        refreshUsers();
    }

    private void refreshUsers() {
        loadRegularUsers(users);
        updateRegularUserList(userList, users);
    }
}
