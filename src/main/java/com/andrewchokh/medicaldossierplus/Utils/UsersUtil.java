package com.andrewchokh.medicaldossierplus.Utils;

import static com.andrewchokh.medicaldossierplus.Config.Config.DECIMAL_FORMAT;

import com.andrewchokh.medicaldossierplus.Database.SQLite;
import com.andrewchokh.medicaldossierplus.Enums.AccountTypes;
import com.andrewchokh.medicaldossierplus.Types.Builders.UserBuilder;
import com.andrewchokh.medicaldossierplus.Types.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class UsersUtil {

    public static void loadRegularUsers(List<User> users) {
        users.clear();

        List<Object> query = SQLite.executeQuery(
            "SELECT * FROM Users WHERE account_type = %d"
                .formatted(AccountTypes.REGULAR.getValue())
        );

        if (query.isEmpty()) return;

        for (int i = query.size() - 1; i >= 0 ; i--) {
            HashMap<String, Object> userData = (HashMap<String, Object>) query.get(i);

            User user = new UserBuilder()
                .id((int) userData.get("id"))
                .firstName((String) userData.get("first_name"))
                .lastName((String) userData.get("last_name"))
                .email((String) userData.get("email"))
                .password((String) userData.get("password"))
                .accountType((int) userData.get("account_type"))
                .build();

            users.add(user);
        }
    }

    public static void updateRegularUserList(ListView regularUserList, List<User> users) {
        ObservableList<String> recordListItems = regularUserList.getItems();

        if (!recordListItems.isEmpty()) {
            recordListItems.remove(0, recordListItems.size());
        }

        for (int i = users.size() - 1; i >= 0 ; i--) {
            recordListItems.add(String.format(
                "%s %s", users.get(i).getFirstName(), users.get(i).getLastName()
            ));
        }

        regularUserList.setItems(recordListItems);
    }

    public static void calculateCharacteristics(int id, Label weightLabel, Label pulseLabel, Label pressureLabel, Label temperatureLabel) {
        List<Object> query = SQLite.executeQuery(
            "SELECT * FROM Records WHERE regular_user_id = %d"
                .formatted(id)
        );

        if (query.isEmpty()) {
            weightLabel.setText("Weight: -");
            pulseLabel.setText("Pulse: -");
            pressureLabel.setText("Blood pressure: -");
            temperatureLabel.setText("Body temperature: -");

            return;
        }

        List<Float> weightRecords = new ArrayList<>();
        List<Integer> pulseRecords = new ArrayList<>();
        List<Integer> systolicBloodPressureRecords = new ArrayList<>();
        List<Integer> diastolicBloodPressureRecords = new ArrayList<>();
        List<Float> bodyTemperatureRecords = new ArrayList<>();

        for (int i = 0; i < query.size(); i++) {
            HashMap<String, Object> recordData = (HashMap<String, Object>) query.get(i);

            weightRecords.add(((Double) recordData.get("weight")).floatValue());
            pulseRecords.add((int) recordData.get("pulse"));
            systolicBloodPressureRecords.add((int) recordData.get("systolic_blood_pressure"));
            diastolicBloodPressureRecords.add((int) recordData.get("diastolic_blood_pressure"));
            bodyTemperatureRecords.add(((Double) recordData.get("body_temperature")).floatValue());
        }

        float weightValue = (float) getAverageNumberOfArray(weightRecords);
        int pulseValue = (int) getAverageNumberOfArray(pulseRecords);
        int systolicBloodPressureValue = (int) getAverageNumberOfArray(systolicBloodPressureRecords);
        int diastolicBloodPressureValue = (int) getAverageNumberOfArray(diastolicBloodPressureRecords);
        float bodyTemperatureValue = (float) getAverageNumberOfArray(bodyTemperatureRecords);

        weightLabel.setText("Weight: %s kg".formatted(DECIMAL_FORMAT.format(weightValue)));
        pulseLabel.setText("Pulse: %d bpm".formatted(pulseValue));
        pressureLabel.setText("Blood pressure: %d / %d"
            .formatted(systolicBloodPressureValue, diastolicBloodPressureValue)
        );
        temperatureLabel.setText("Body temperature: %s °C".formatted(DECIMAL_FORMAT.format(bodyTemperatureValue)));
    }

    public static  <T extends Number> Number getAverageNumberOfArray(List<T> list) {
        if (list.isEmpty()) return 0;

        if (list.get(0) instanceof Float) {
            float sum = 0;

            for (T value : list) {
                sum += (Float) value;
            }

            return sum / list.size();
        }
        else if (list.get(0) instanceof Integer) {
            int sum = 0;

            for (T value : list) {
                sum += (Integer) value;
            }

            return Math.round((float) sum / list.size());
        }

        return 0;
    }
}
