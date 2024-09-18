package com.andrewchokh.medicaldossierplus.Controllers.Client;

import static com.andrewchokh.medicaldossierplus.App.currentUser;

import com.andrewchokh.medicaldossierplus.Database.SQLite;
import com.andrewchokh.medicaldossierplus.Types.Builders.DiaryEntryBuilder;
import com.andrewchokh.medicaldossierplus.Types.DiaryEntry;
import com.andrewchokh.medicaldossierplus.Utils.DatabaseUtil;
import com.andrewchokh.medicaldossierplus.Utils.DateUtil;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
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
import javafx.scene.control.TextArea;

public class ClientDiaryController implements Initializable {

    public Button createButton;
    public Button editButton;
    public Button removeButton;
    public Button refreshButton;
    public TextArea diaryArea;
    public Label dateLabel;
    public ListView entriesList;
    public FontAwesomeIconView createButtonIcon;
    public FontAwesomeIconView editButtonIcon;

    private ArrayList<DiaryEntry> diaryEntries = new ArrayList<>();

    private Boolean createMode = false;
    private Boolean editMode = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createButton.setOnAction(actionEvent -> createEntry());
        editButton.setOnAction(actionEvent -> editEntry());
        removeButton.setOnAction(actionEvent -> removeEntry());
        refreshButton.setOnAction(actionEvent -> refreshEntryList());

        entriesList.getSelectionModel().selectedItemProperty().addListener(this::switchEntry);

        loadDiaryEntries();
        updateEntryList();
    }

    private void switchEntry(ObservableValue<String> observableValue, Object o, Object o1) {
        int index = entriesList.getItems().indexOf(observableValue.getValue());

        if (index < 0) {
            diaryArea.setText("");
            dateLabel.setText("Creation date: -");
            return;
        }

        DiaryEntry entry = diaryEntries.get(index);

        if (entry != null) {
            diaryArea.setText(entry.getContent());
            dateLabel.setText("Creation date: %s".formatted(entry.getCreationDate().toString()));
        }
    }

    private void createEntry() {
        createMode = !createMode;

        if (createMode) {
            changeControlsActivity();
            createButton.setDisable(false);
            diaryArea.setDisable(false);

            diaryArea.setEditable(true);
            diaryArea.clear();
            diaryArea.setPromptText("Write your entry here!");

            createButton.setText("Save");
            createButtonIcon.setGlyphName("SAVE");
        }
        else {
            SQLite.executeUpdate(
                "INSERT INTO DiaryEntries (id, user_id, content, creation_date) VALUES (%d, %d, '%s', '%s')"
                    .formatted(
                        DatabaseUtil.generateRandomID(),
                        currentUser.getId(),
                        diaryArea.getText(),
                        DateUtil.dateToString(DateUtil.getCurrentDate())
                    )
            );

            changeControlsActivity();
            createButton.setDisable(false);
            diaryArea.setDisable(false);

            diaryArea.setEditable(false);
            diaryArea.clear();
            diaryArea.setPromptText("");

            createButton.setText("Create");
            createButtonIcon.setGlyphName("PENCIL");
        }

        refreshEntryList();
    }

    private void editEntry() {
        String name = (String) entriesList.getSelectionModel().selectedItemProperty().getValue();

        if (name == null) return;

        int index = entriesList.getItems().indexOf(name);

        editMode = !editMode;

        if (editMode) {
            changeControlsActivity();
            editButton.setDisable(false);
            diaryArea.setDisable(false);

            diaryArea.setEditable(true);
            editButton.setText("Save");
            editButtonIcon.setGlyphName("SAVE");
        }
        else {
            SQLite.executeUpdate(
                "UPDATE DiaryEntries SET content = '%s' WHERE id = '%d'"
                    .formatted(diaryArea.getText(), diaryEntries.get(index).getId())
            );

            changeControlsActivity();
            editButton.setDisable(false);
            diaryArea.setDisable(false);

            diaryArea.setEditable(false);
            editButton.setText("Edit");
            editButtonIcon.setGlyphName("PENCIL");
        }
    }

    private void removeEntry() {
        String name = (String) entriesList.getSelectionModel().selectedItemProperty().getValue();

        if (name == null) return;

        int index = entriesList.getItems().indexOf(name);

        SQLite.executeUpdate(
            "DELETE FROM DiaryEntries WHERE id = %d"
            .formatted(diaryEntries.get(index).getId())
        );

        entriesList.getItems().remove(index, index + 1);

        refreshEntryList();
    }

    private void refreshEntryList() {
        loadDiaryEntries();
        updateEntryList();
    }

    private void loadDiaryEntries() {
        diaryEntries.clear();

        List<Object> query = SQLite.executeQuery(String.format(
            "SELECT * FROM DiaryEntries WHERE user_id = '%d'",
            currentUser.getId()
        ));

        if (query.isEmpty()) return;

        for (int i = query.size() - 1; i >= 0 ; i--) {
            HashMap<String, Object> diaryEntryData = (HashMap<String, Object>) query.get(i);

            Date creationDate = DateUtil.stringToDate((String) diaryEntryData.get("creation_date"));

            DiaryEntry entry = new DiaryEntryBuilder()
                .id((int) diaryEntryData.get("id"))
                .userId((int) diaryEntryData.get("user_id"))
                .content((String) diaryEntryData.get("content"))
                .creationDate(creationDate)
                .build();

            diaryEntries.add(entry);
        }
    }

    private void updateEntryList() {
        ObservableList<String> entries = entriesList.getItems();

        if (!entries.isEmpty()) {
            entries.remove(0, entries.size());
        }

        for (int i = diaryEntries.size() - 1; i >= 0 ; i--) {
            entries.add(String.format("Entry #%d", i + 1));
        }

        entriesList.setItems(entries);
    }

    private void changeControlsActivity() {
        entriesList.setDisable(!entriesList.isDisabled());
        editButton.setDisable(!editButton.isDisabled());
        removeButton.setDisable(!removeButton.isDisabled());
        refreshButton.setDisable(!refreshButton.isDisabled());
        diaryArea.setDisable(!diaryArea.isDisabled());
        entriesList.setDisable(!entriesList.isDisabled());
    }
}
