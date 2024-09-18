package com.andrewchokh.medicaldossierplus.Utils;

import static com.andrewchokh.medicaldossierplus.App.currentUser;
import static com.andrewchokh.medicaldossierplus.Utils.DatabaseUtil.getUserById;

import com.andrewchokh.medicaldossierplus.Database.SQLite;
import com.andrewchokh.medicaldossierplus.Enums.AccountTypes;
import com.andrewchokh.medicaldossierplus.Types.Builders.DiaryEntryBuilder;
import com.andrewchokh.medicaldossierplus.Types.DiaryEntry;
import com.andrewchokh.medicaldossierplus.Types.User;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

public class DiaryUtil {
    public static void loadDiaryEntries(List<DiaryEntry> diaryEntries) {
        diaryEntries.clear();

        String sql = "";

        if (currentUser.getAccountType() == AccountTypes.REGULAR.getValue()) {
            sql = "SELECT * FROM DiaryEntries WHERE user_id = '%d'".formatted(currentUser.getId());
        }
        else if (currentUser.getAccountType() == AccountTypes.ADMIN.getValue()) {
            sql = "SELECT * FROM DiaryEntries";
        }

        List<Object> query = SQLite.executeQuery(sql);

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

    public static void updateEntryList(ListView entryList, List<DiaryEntry> diaryEntries) {
        ObservableList<String> entries = entryList.getItems();

        if (!entries.isEmpty()) {
            entries.remove(0, entries.size());
        }

        for (int i = diaryEntries.size() - 1; i >= 0 ; i--) {
            if (currentUser.getAccountType() == AccountTypes.REGULAR.getValue()) {
                entries.add(String.format("Entry #%d", i + 1));
            }
            else if (currentUser.getAccountType() == AccountTypes.ADMIN.getValue()) {
                User author = getUserById(diaryEntries.get(i).getUserId());

                if (author != null) {
                    entries.add(String.format("Entry of %s #%d", author.getFirstName() , i + 1));
                }
                else {
                    entries.add(String.format("Entry #%d", i + 1));
                }
            }
        }

        entryList.setItems(entries);
    }

    public static void saveEntry(DiaryEntry diaryEntry) {
        SQLite.executeUpdate(
            """
                INSERT INTO DiaryEntries (id, user_id, content, creation_date) 
                VALUES (%d, %d, '%s', '%s');
            """
                .formatted(
                    diaryEntry.getId(), diaryEntry.getUserId(),
                    diaryEntry.getContent(), DateUtil.dateToString(diaryEntry.getCreationDate())
                )
        );
    }
}
