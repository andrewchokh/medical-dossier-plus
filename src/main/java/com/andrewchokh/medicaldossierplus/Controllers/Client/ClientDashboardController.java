package com.andrewchokh.medicaldossierplus.Controllers.Client;

import static com.andrewchokh.medicaldossierplus.App.currentUser;
import static com.andrewchokh.medicaldossierplus.Config.Config.DECIMAL_FORMAT;

import com.andrewchokh.medicaldossierplus.App;
import com.andrewchokh.medicaldossierplus.Database.SQLite;
import com.andrewchokh.medicaldossierplus.Utils.DatabaseUtil;
import com.andrewchokh.medicaldossierplus.Utils.DateUtil;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class ClientDashboardController implements Initializable {

    public Label nameLabel;
    public TextArea diaryArea;
    public Label weightLabel;
    public Label pulseLabel;
    public Label pressureLabel;
    public Label temperatureLabel;
    public ScrollPane recordsPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        diaryArea.setOnKeyPressed(this::saveDiaryEntry);

        setNameLabel();
        calculateCharacteristics();
    }

    private void saveDiaryEntry(KeyEvent keyEvent) {
        if (keyEvent.getCode() != KeyCode.ENTER) return;

        SQLite.executeUpdate(
            "INSERT INTO DiaryEntries (id, user_id, content, creation_date) VALUES (%d, %d, '%s', '%s')"
                .formatted(
                    DatabaseUtil.generateRandomID(),
                    currentUser.getId(),
                    diaryArea.getText(),
                    DateUtil.dateToString(DateUtil.getCurrentDate())
                )
        );

        nameLabel.requestFocus();

        diaryArea.clear();
        diaryArea.setPromptText("The entry has been saved.");
    }

    private void setNameLabel() {
        nameLabel.setText(App.currentUser.getFirstName());
    }

    private void calculateCharacteristics() {
        List<Object> query = SQLite.executeQuery(
            "SELECT * FROM Records WHERE regular_user_id = %d"
                .formatted(currentUser.getId())
        );

        if (query.isEmpty()) return;

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

    private <T extends Number> Number getAverageNumberOfArray(List<T> list) {
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
