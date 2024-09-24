package com.andrewchokh.medicaldossierplus.Utils;

import static com.andrewchokh.medicaldossierplus.App.currentUser;
import static com.andrewchokh.medicaldossierplus.Config.Config.DECIMAL_FORMAT;

import com.andrewchokh.medicaldossierplus.Database.SQLite;
import com.andrewchokh.medicaldossierplus.Enums.AccountTypes;
import com.andrewchokh.medicaldossierplus.Types.Builders.RecordBuilder;
import com.andrewchokh.medicaldossierplus.Types.Record;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

public class RecordsUtil {
    public static void loadRecords(List<Record> records) {
        records.clear();

        String sql = "";

        if (currentUser.getAccountType() == AccountTypes.REGULAR.getValue()) {
            sql = "SELECT * FROM Records WHERE regular_user_id = '%d'".formatted(currentUser.getId());
        }
        else if (currentUser.getAccountType() == AccountTypes.ADMIN.getValue()) {
            sql = "SELECT * FROM Records WHERE admin_user_id = '%d'".formatted(currentUser.getId());
        }

        List<Object> query = SQLite.executeQuery(sql);

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
                .weight(((Double) recordData.get("weight")).floatValue())
                .pulse((int) recordData.get("pulse"))
                .systolicBloodPressure((int) recordData.get("systolic_blood_pressure"))
                .diastolicBloodPressure((int) recordData.get("systolic_blood_pressure"))
                .bodyTemperature(((Double) recordData.get("body_temperature")).floatValue())
                .build();

            records.add(record);
        }
    }

    public static void updateRecordList(ListView recordList, List<Record> records) {
        ObservableList<String> recordListItems = recordList.getItems();

        if (!recordListItems.isEmpty()) {
            recordListItems.remove(0, recordListItems.size());
        }

        for (int i = records.size() - 1; i >= 0 ; i--) {
            recordListItems.add(String.format("Record #%d", i + 1));
        }

        recordList.setItems(recordListItems);
    }

    public static void createRecord(Record record) {
        SQLite.executeUpdate(
            """
                INSERT INTO Records (id, regular_user_id, admin_user_id, content, creation_date,
                weight, pulse, systolic_blood_pressure, diastolic_blood_pressure, body_temperature)
                VALUES (%d, %d, %d, '%s', '%s', %s, %d, %d, %d, %s)
            """.formatted(
                record.getId(), record.getRegularUserId(), record.getAdminUserId(),
                record.getContent(), DateUtil.dateToString(record.getCreationDate()),
                DECIMAL_FORMAT.format(record.getWeight()).replace(',', '.'), record.getPulse(), record.getSystolicBloodPressure(),
                record.getDiastolicBloodPressure(), DECIMAL_FORMAT.format(record.getBodyTemperature()).replace(',', '.')
            )
        );
    }

    public static void updateRecord(int id, String content, float weight, int pulse, int systolic_blood_pressure,
        int diastolic_blood_pressure, float body_temperature) {
        SQLite.executeUpdate(
            """
                UPDATE Records SET content = '%s', weight = '%s', pulse = %d, systolic_blood_pressure = %d, 
                diastolic_blood_pressure = %d, body_temperature = '%s' WHERE id = '%d'"""
                .formatted(content, DECIMAL_FORMAT.format(weight).replace(',', '.'),
                    pulse, systolic_blood_pressure, diastolic_blood_pressure,
                    DECIMAL_FORMAT.format(body_temperature).replace(',', '.'), id)
        );
    }
}
