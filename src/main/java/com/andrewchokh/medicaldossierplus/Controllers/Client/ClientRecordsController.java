package com.andrewchokh.medicaldossierplus.Controllers.Client;

import static com.andrewchokh.medicaldossierplus.App.currentUser;

import com.andrewchokh.medicaldossierplus.Database.SQLite;
import com.andrewchokh.medicaldossierplus.Types.Builders.DiaryEntryBuilder;
import com.andrewchokh.medicaldossierplus.Types.Builders.RecordBuilder;
import com.andrewchokh.medicaldossierplus.Types.DiaryEntry;
import com.andrewchokh.medicaldossierplus.Types.Record;
import com.andrewchokh.medicaldossierplus.Utils.DatabaseUtil;
import com.andrewchokh.medicaldossierplus.Utils.DateUtil;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;

public class ClientRecordsController implements Initializable {

    public Label dateLabel;
    public TextArea recordArea;
    public ListView recordList;

    private List<Record> records = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadRecords();
        updateRecordList();

        recordList.getSelectionModel().selectedItemProperty().addListener(this::switchRecord);
    }

    private void loadRecords() {
        records.clear();

        List<Object> query = SQLite.executeQuery(String.format(
            "SELECT * FROM Records WHERE regular_user_id = '%d'",
            currentUser.getId()
        ));

        if (query.isEmpty()) return;

        for (int i = query.size() - 1; i >= 0 ; i--) {
            HashMap<String, Object> recordData = (HashMap<String, Object>) query.get(i);

            Date creationDate = DateUtil.stringToDate((String) recordData.get("creation_date"));

            Record record = new RecordBuilder()
                .id((int) recordData.get("id"))
                .regularUserId((int) recordData.get("regular_user_id"))
                .adminUserId((int) recordData.get("admin_user_id"))
                .content((String) recordData.get("content"))
                .creationDate(creationDate)
                .weight((float) recordData.get("weight"))
                .pulse((float) recordData.get("pulse"))
                .systolicBloodPressure((int) recordData.get("systolic_blood_pressure"))
                .diastolicBloodPressure((int) recordData.get("systolic_blood_pressure"))
                .bodyTemperature((float) recordData.get("body_temperature"))
                .build();

            records.add(record);
        }
    }

    private void updateRecordList() {
        ObservableList<String> recordListItems = recordList.getItems();

        if (!recordListItems.isEmpty()) {
            recordListItems.remove(0, recordListItems.size());
        }

        for (int i = records.size() - 1; i >= 0 ; i--) {
            recordListItems.add(String.format("Entry #%d", i + 1));
        }

        recordList.setItems(recordListItems);
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
}
