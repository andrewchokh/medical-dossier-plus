package com.andrewchokh.medicaldossierplus.Controllers.Client;

import static com.andrewchokh.medicaldossierplus.Utils.RecordsUtil.loadRecords;
import static com.andrewchokh.medicaldossierplus.Utils.RecordsUtil.updateRecordList;

import com.andrewchokh.medicaldossierplus.Types.Record;
import com.andrewchokh.medicaldossierplus.Types.User;
import com.andrewchokh.medicaldossierplus.Utils.DatabaseUtil;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class ClientRecordsController implements Initializable {

    public Label dateLabel;
    public Label authorLabel;
    public TextArea recordArea;
    public ListView recordList;

    private final List<Record> records = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadRecords(records);
        updateRecordList(recordList, records);

        recordList.getSelectionModel().selectedItemProperty().addListener(this::switchRecord);
    }

    private void switchRecord(ObservableValue<String> observableValue, Object o, Object o1) {
        int index = recordList.getItems().indexOf(observableValue.getValue());

        if (index < 0) {
            recordArea.clear();
            dateLabel.setText("Creation date: -");
            return;
        }

        Record record = records.get(index);
        User author = DatabaseUtil.getUserById(record.getAdminUserId());

        if (record != null) {
            recordArea.setText(record.getContent());
            dateLabel.setText("Creation date: %s".formatted(record.getCreationDate()));
            authorLabel.setText("Author: %s %s".formatted(author.getFirstName(), author.getLastName()));
        }
    }
}
