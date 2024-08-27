module com.andrewchokh.medicaldossierplus {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.andrewchokh.medicaldossierplus to javafx.fxml;
    exports com.andrewchokh.medicaldossierplus;
}