package com.andrewchokh.medicaldossierplus.Controllers.Admin;

import static com.andrewchokh.medicaldossierplus.App.currentUser;
import static com.andrewchokh.medicaldossierplus.Utils.DatabaseUtil.getUserByEmail;
import static com.andrewchokh.medicaldossierplus.Utils.RecordsUtil.createRecord;
import static com.andrewchokh.medicaldossierplus.Utils.RecordsUtil.loadRecords;
import static com.andrewchokh.medicaldossierplus.Utils.RecordsUtil.updateRecord;
import static com.andrewchokh.medicaldossierplus.Utils.RecordsUtil.updateRecordList;

import com.andrewchokh.medicaldossierplus.Database.SQLite;
import com.andrewchokh.medicaldossierplus.Types.Builders.RecordBuilder;
import com.andrewchokh.medicaldossierplus.Types.Record;
import com.andrewchokh.medicaldossierplus.Types.User;
import com.andrewchokh.medicaldossierplus.Utils.DatabaseUtil;
import com.andrewchokh.medicaldossierplus.Utils.DateUtil;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class AdminRecordsController implements Initializable {

    public Button createButton;
    public Button editButton;
    public Button removeButton;
    public TextArea recordArea;
    public TextField bloodPressureField;
    public TextField bodyTemperatureField;
    public TextField weightField;
    public TextField pulseField;
    public TextField userEmailField;
    public Label dateLabel;
    public ListView recordList;
    public FontAwesomeIconView createButtonIcon;
    public FontAwesomeIconView editButtonIcon;

    private final List<Record> records = new ArrayList<>();

    private Boolean createMode = false;
    private Boolean editMode = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createButton.setOnAction(actionEvent -> createEntry());
        editButton.setOnAction(actionEvent -> editEntry());
        removeButton.setOnAction(actionEvent -> removeEntry());

        recordList.getSelectionModel().selectedItemProperty().addListener(this::switchRecord);

        refreshEntries();
    }

    private void createEntry() {
        createMode = !createMode;

        changeControlsActivity();
        enableCreateModeControls();

        if (createMode) {
            recordArea.setEditable(true);
            createButton.setText("Save");
            createButtonIcon.setGlyphName("SAVE");
        }
        else {
            User user = getUserByEmail(userEmailField.getText());

            if (user == null) return;

            String[] bloodPressure = bloodPressureField.getText().split("/");

            Record record = new RecordBuilder()
                .id(DatabaseUtil.generateRandomID())
                .regularUserId(user.getId())
                .adminUserId(currentUser.getId())
                .content(recordArea.getText())
                .creationDate(DateUtil.getCurrentDate())
                .weight(Float.parseFloat(weightField.getText()))
                .pulse(Integer.parseInt(pulseField.getText()))
                .systolicBloodPressure(Integer.parseInt(bloodPressure[0]))
                .diastolicBloodPressure(Integer.parseInt(bloodPressure[1]))
                .bodyTemperature(Float.parseFloat(bodyTemperatureField.getText()))
                .build();

            createRecord(record);

            recordArea.setEditable(false);
            createButton.setText("Create");
            createButtonIcon.setGlyphName("FILE");
        }

        refreshEntries();
    }

    private void editEntry() {
        String name = (String) recordList.getSelectionModel().selectedItemProperty().getValue();

        if (name == null) return;

        int index = recordList.getItems().indexOf(name);

        editMode = !editMode;

        changeControlsActivity();
        enableEditModeControls();

        if (editMode) {
            recordArea.setEditable(true);
            editButton.setText("Save");
            editButtonIcon.setGlyphName("SAVE");
        }
        else {
            updateRecord(records.get(index).getId(), recordArea.getText());

            recordArea.setEditable(false);
            editButton.setText("Edit");
            editButtonIcon.setGlyphName("PENCIL");
        }
    }

    private void removeEntry() {
        String name = (String) recordList.getSelectionModel().selectedItemProperty().getValue();

        if (name == null) return;

        int index = recordList.getItems().indexOf(name);

        SQLite.executeUpdate(
            "DELETE FROM DiaryEntries WHERE id = %d"
                .formatted(records.get(index).getId())
        );

        recordList.getItems().remove(index, index + 1);

        refreshEntries();
    }

    private void switchRecord(ObservableValue<String> observableValue, Object o, Object o1) {
        int index = recordList.getItems().indexOf(observableValue.getValue());

        if (index < 0) {
            recordArea.clear();
            dateLabel.setText("Creation date: -");
            return;
        }

        Record record = records.get(index);

        if (record != null) {
            recordArea.setText(record.getContent());
            dateLabel.setText("Creation date: %s".formatted(record.getCreationDate()));
        }
    }

    private void refreshEntries() {
        loadRecords(records);
        updateRecordList(recordList, records);
    }

    private void changeControlsActivity() {
        recordList.setDisable(!recordList.isDisabled());
        createButton.setDisable(!createButton.isDisabled());
        editButton.setDisable(!editButton.isDisabled());
        removeButton.setDisable(!removeButton.isDisabled());

        recordArea.setEditable(!recordArea.isEditable());
        userEmailField.setEditable(!userEmailField.isEditable());
        bloodPressureField.setEditable(!bloodPressureField.isEditable());
        weightField.setEditable(!weightField.isEditable());
        bodyTemperatureField.setEditable(!bodyTemperatureField.isEditable());
        pulseField.setEditable(!pulseField.isEditable());
    }

    private void enableCreateModeControls() {
        createButton.setDisable(false);
        recordArea.setDisable(false);
    }

    private void enableEditModeControls() {
        editButton.setDisable(false);
        recordArea.setDisable(false);
        bloodPressureField.setDisable(false);
        weightField.setDisable(false);
        bodyTemperatureField.setDisable(false);
        pulseField.setDisable(false);
    }
}
