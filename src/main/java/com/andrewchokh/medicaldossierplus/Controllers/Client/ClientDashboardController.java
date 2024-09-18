package com.andrewchokh.medicaldossierplus.Controllers.Client;

import static com.andrewchokh.medicaldossierplus.App.currentUser;
import static com.andrewchokh.medicaldossierplus.Config.Config.DECIMAL_FORMAT;
import static com.andrewchokh.medicaldossierplus.Utils.DiaryUtil.saveEntry;
import static com.andrewchokh.medicaldossierplus.Utils.UsersUtil.calculateCharacteristics;

import com.andrewchokh.medicaldossierplus.App;
import com.andrewchokh.medicaldossierplus.Database.SQLite;
import com.andrewchokh.medicaldossierplus.Types.Builders.DiaryEntryBuilder;
import com.andrewchokh.medicaldossierplus.Types.DiaryEntry;
import com.andrewchokh.medicaldossierplus.Utils.DatabaseUtil;
import com.andrewchokh.medicaldossierplus.Utils.DateUtil;
import com.andrewchokh.medicaldossierplus.Utils.DiaryUtil;
import com.andrewchokh.medicaldossierplus.Utils.UsersUtil;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class ClientDashboardController implements Initializable {

    public Label nameLabel;
    public TextArea diaryArea;
    public Label weightLabel;
    public Label pulseLabel;
    public Label pressureLabel;
    public Label temperatureLabel;
    public ScrollPane recordsPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        diaryArea.setOnKeyPressed(this::saveDiaryEntry);

        setNameLabel();
        calculateCharacteristics(currentUser.getId(), weightLabel, pulseLabel, pressureLabel, temperatureLabel);
    }

    private void saveDiaryEntry(KeyEvent keyEvent) {
        if (keyEvent.getCode() != KeyCode.ENTER) return;

        DiaryEntry diaryEntry = new DiaryEntryBuilder()
            .id(DatabaseUtil.generateRandomID())
            .userId(currentUser.getId())
            .content(diaryArea.getText())
            .creationDate(DateUtil.getCurrentDate())
            .build();

        saveEntry(diaryEntry);

        nameLabel.requestFocus();

        diaryArea.clear();
        diaryArea.setPromptText("The entry has been saved.");
    }

    private void setNameLabel() {
        nameLabel.setText(App.currentUser.getFirstName());
    }
}
