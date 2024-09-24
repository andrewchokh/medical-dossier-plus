package com.andrewchokh.medicaldossierplus.Controllers.Admin;

import static com.andrewchokh.medicaldossierplus.App.currentUser;
import static com.andrewchokh.medicaldossierplus.Utils.DatabaseUtil.getUserByEmail;
import static com.andrewchokh.medicaldossierplus.Utils.DatabaseUtil.getUserById;
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
    private int recordEditIndex = -1;

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

        if (createMode) {
            recordArea.clear();
            createButton.setText("Save");
            createButtonIcon.setGlyphName("SAVE");

            changeControlsActivity();
            enableCreateModeControls();
        }
        else {
            User user = getUserByEmail(userEmailField.getText());

            if (user == null) {
                createMode = true;
                return;
            }

            Record record;

            try {
                String[] bloodPressure = bloodPressureField.getText().split("/");

                record = new RecordBuilder()
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

                if (!validateRecord(record.getContent(), record.getWeight(), record.getPulse(),
                    record.getSystolicBloodPressure(), record.getDiastolicBloodPressure(),
                    record.getBodyTemperature())
                ) {
                    createMode = true;
                    return;
                }
            }
            catch (Exception e) {
                createMode = true;
                return;
            }

            createRecord(record);

            createButton.setText("Create");
            createButtonIcon.setGlyphName("FILE");

            changeControlsActivity();
            enableCreateModeControls();
        }

        refreshEntries();
    }

    private void editEntry() {
        editMode = !editMode;

        if (editMode) {
            String name = (String) recordList.getSelectionModel().selectedItemProperty().getValue();

            if (name == null) {
                editMode = false;
                return;
            }

            recordEditIndex = recordList.getItems().indexOf(name);

            editButton.setText("Save");
            editButtonIcon.setGlyphName("SAVE");

            changeControlsActivity();
            enableEditModeControls();
        }
        else {
            Record record = records.get(recordEditIndex);

            if (!validateRecord(recordArea.getText(), record.getWeight(), record.getPulse(),
                record.getSystolicBloodPressure(), record.getDiastolicBloodPressure(),
                record.getBodyTemperature())
            ) {
                editMode = true;
                return;
            }

            updateRecord(
                record.getId(), recordArea.getText(), record.getWeight(), record.getPulse(),
                record.getSystolicBloodPressure(), record.getDiastolicBloodPressure(), record.getBodyTemperature()
            );

            editButton.setText("Edit");
            editButtonIcon.setGlyphName("PENCIL");

            changeControlsActivity();
            enableEditModeControls();
        }

        refreshEntries();
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

        if (editMode) {
            return;
        }

        if (index < 0) {
            recordArea.clear();
            dateLabel.setText("Creation date: -");
            return;
        }

        Record record = records.get(index);

        if (record == null) return;

        User regularUser = getUserById(record.getRegularUserId());

        if (regularUser == null) return;

        recordArea.setText(record.getContent());
        weightField.setText(Float.toString(record.getWeight()));
        pulseField.setText(Integer.toString(record.getPulse()));
        bloodPressureField.setText(
            "%d/%d"
            .formatted(record.getSystolicBloodPressure(), record.getDiastolicBloodPressure())
        );
        bodyTemperatureField.setText(Float.toString(record.getBodyTemperature()));
        userEmailField.setText(regularUser.getEmail());
        dateLabel.setText("Creation date: %s".formatted(record.getCreationDate()));
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

    private Boolean validateRecord(String content, float weight, int pulse, int systolicBloodPressure,
        int diastolicBloodPressure, float bodyTemperature) {
        //Content validation
        if (content.isEmpty()) {
            return false;
        }

        // Weight validation
        if (!(weight >= 0f && weight <= 300f)) {
            return false;
        }

        // Pulse validation
        if (!(pulse >= 0 && pulse <= 300)) {
            return false;
        }

        // Blood pressure validation
        if (!(systolicBloodPressure >= 0 && systolicBloodPressure <= 500) ||
            !(diastolicBloodPressure > 0 && diastolicBloodPressure <= 500)) {
            return false;
        }

        // Body temperature validation
        if (!(bodyTemperature >= 0 && bodyTemperature <= 100f)) {
            return false;
        }

        return true;
    }
}
