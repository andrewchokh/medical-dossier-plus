module com.andrewchokh.medicaldossierplus {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    opens com.andrewchokh.medicaldossierplus to javafx.fxml;
    exports com.andrewchokh.medicaldossierplus;
    exports com.andrewchokh.medicaldossierplus.Controllers;
    exports com.andrewchokh.medicaldossierplus.Controllers.Admin;
    exports com.andrewchokh.medicaldossierplus.Controllers.Client;
    exports com.andrewchokh.medicaldossierplus.Models;
    exports com.andrewchokh.medicaldossierplus.Views;
    exports com.andrewchokh.medicaldossierplus.Enums;
}