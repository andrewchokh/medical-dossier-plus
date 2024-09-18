package com.andrewchokh.medicaldossierplus.Controllers.Admin;

import static com.andrewchokh.medicaldossierplus.Utils.DiaryUtil.loadDiaryEntries;
import static com.andrewchokh.medicaldossierplus.Utils.DiaryUtil.updateEntryList;
import static com.andrewchokh.medicaldossierplus.Utils.RecordsUtil.loadRecords;
import static com.andrewchokh.medicaldossierplus.Utils.RecordsUtil.updateRecordList;

import com.andrewchokh.medicaldossierplus.Types.DiaryEntry;
import com.andrewchokh.medicaldossierplus.Types.Record;
import com.andrewchokh.medicaldossierplus.Types.User;
import com.andrewchokh.medicaldossierplus.Utils.DatabaseUtil;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;

public class AdminDiaryController implements Initializable {

    public TextArea diaryArea;
    public Label dateLabel;
    public Label authorLabel;
    public ListView diaryEntryList;

    private final List<DiaryEntry> diaryEntries = new ArrayList<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadDiaryEntries(diaryEntries);
        updateEntryList(diaryEntryList, diaryEntries);

        diaryEntryList.getSelectionModel().selectedItemProperty().addListener(this::switchRecord);
    }

    private void switchRecord(ObservableValue<String> observableValue, Object o, Object o1) {
        int index = diaryEntryList.getItems().indexOf(observableValue.getValue());

        if (index < 0) {
            diaryArea.clear();
            dateLabel.setText("Creation date: -");
            return;
        }

        DiaryEntry diaryEntry = diaryEntries.get(index);
        User author = DatabaseUtil.getUserById(diaryEntry.getUserId());

        if (diaryEntry != null) {
            diaryArea.setText(diaryEntry.getContent());
            dateLabel.setText("Creation date: %s".formatted(diaryEntry.getCreationDate()));
            authorLabel.setText("Author: %s %s".formatted(author.getFirstName(), author.getLastName()));
        }
    }
}
