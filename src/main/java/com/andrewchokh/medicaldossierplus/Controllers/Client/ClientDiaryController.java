package com.andrewchokh.medicaldossierplus.Controllers.Client;

import static com.andrewchokh.medicaldossierplus.App.currentUser;
import static com.andrewchokh.medicaldossierplus.Utils.DateUtil.dateToString;
import static com.andrewchokh.medicaldossierplus.Utils.DiaryUtil.loadDiaryEntries;
import static com.andrewchokh.medicaldossierplus.Utils.DiaryUtil.saveEntry;
import static com.andrewchokh.medicaldossierplus.Utils.DiaryUtil.updateEntryList;

import com.andrewchokh.medicaldossierplus.Database.SQLite;
import com.andrewchokh.medicaldossierplus.Types.Builders.DiaryEntryBuilder;
import com.andrewchokh.medicaldossierplus.Types.DiaryEntry;
import com.andrewchokh.medicaldossierplus.Utils.DatabaseUtil;
import com.andrewchokh.medicaldossierplus.Utils.DateUtil;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class ClientDiaryController implements Initializable {

    public Button createButton;
    public Button editButton;
    public Button removeButton;
    public TextArea diaryArea;
    public Label dateLabel;
    public ListView entriesList;
    public FontAwesomeIconView createButtonIcon;
    public FontAwesomeIconView editButtonIcon;

    private final ArrayList<DiaryEntry> diaryEntries = new ArrayList<>();

    private Boolean createMode = false;
    private Boolean editMode = false;
    private int entryEditIndex = -1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createButton.setOnAction(actionEvent -> createEntry());
        editButton.setOnAction(actionEvent -> editEntry());
        removeButton.setOnAction(actionEvent -> removeEntry());

        entriesList.getSelectionModel().selectedItemProperty().addListener(this::switchEntry);

        refreshEntries();
    }

    private void switchEntry(ObservableValue<String> observableValue, Object o, Object o1) {
        int index = entriesList.getItems().indexOf(observableValue.getValue());

        if (editMode) {
            return;
        }

        if (index < 0) {
            diaryArea.clear();
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
            diaryArea.setEditable(true);
            diaryArea.clear();
            diaryArea.setPromptText("Write your entry here!");

            createButton.setText("Save");
            createButtonIcon.setGlyphName("SAVE");

            changeControlsActivity();
            enableCreateModeControls();
        }
        else {
            DiaryEntry diaryEntry = new DiaryEntryBuilder()
                .id(DatabaseUtil.generateRandomID())
                .userId(currentUser.getId())
                .content(diaryArea.getText())
                .creationDate(DateUtil.getCurrentDate())
                .build();

            saveEntry(diaryEntry);

            diaryArea.setEditable(false);
            diaryArea.clear();
            diaryArea.setPromptText("");

            createButton.setText("Create");
            createButtonIcon.setGlyphName("PENCIL");

            changeControlsActivity();
            enableCreateModeControls();
        }

        refreshEntries();
    }

    private void editEntry() {
        editMode = !editMode;

        if (editMode) {
            String name = (String) entriesList.getSelectionModel().selectedItemProperty().getValue();

            if (name == null) {
                editMode = false;
                return;
            }

            entryEditIndex = entriesList.getItems().indexOf(name);

            diaryArea.setEditable(true);
            diaryArea.setText(diaryEntries.get(entryEditIndex).getContent());
            diaryArea.setText(dateToString(diaryEntries.get(entryEditIndex).getCreationDate()));

            editButton.setText("Save");
            editButtonIcon.setGlyphName("SAVE");

            changeControlsActivity();
            enableEditModeControls();
        }
        else {
            SQLite.executeUpdate(
                "UPDATE DiaryEntries SET content = '%s' WHERE id = '%d'"
                    .formatted(diaryArea.getText(), diaryEntries.get(entryEditIndex).getId())
            );

            diaryArea.setEditable(false);
            editButton.setText("Edit");
            editButtonIcon.setGlyphName("PENCIL");

            changeControlsActivity();
            enableEditModeControls();
        }

        refreshEntries();
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

        refreshEntries();
    }

    private void refreshEntries() {
        loadDiaryEntries(diaryEntries);
        updateEntryList(entriesList, diaryEntries);
    }

    private void changeControlsActivity() {
        entriesList.setDisable(!entriesList.isDisabled());
        createButton.setDisable(!createButton.isDisabled());
        editButton.setDisable(!editButton.isDisabled());
        removeButton.setDisable(!removeButton.isDisabled());
        diaryArea.setDisable(!diaryArea.isDisabled());
    }

    private void enableCreateModeControls() {
        createButton.setDisable(false);
        diaryArea.setDisable(false);
    }

    private void enableEditModeControls() {
        editButton.setDisable(false);
        diaryArea.setDisable(false);
    }
}
