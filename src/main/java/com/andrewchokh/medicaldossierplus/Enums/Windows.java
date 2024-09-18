package com.andrewchokh.medicaldossierplus.Enums;

import com.andrewchokh.medicaldossierplus.Controllers.Admin.AdminController;
import com.andrewchokh.medicaldossierplus.Controllers.Admin.AdminDiaryController;
import com.andrewchokh.medicaldossierplus.Controllers.Admin.AdminRecordsController;
import com.andrewchokh.medicaldossierplus.Controllers.Client.ClientController;
import com.andrewchokh.medicaldossierplus.Controllers.Client.ClientDiaryController;
import com.andrewchokh.medicaldossierplus.Controllers.Client.ClientRecordsController;
import com.andrewchokh.medicaldossierplus.Controllers.LoginController;
import com.andrewchokh.medicaldossierplus.Controllers.SignupController;
import javafx.fxml.Initializable;

public enum Windows {
    LOGIN("/Fxml/Login.fxml"),
    SIGNUP("/Fxml/Signup.fxml"),
    VERIFICATION_CODE("/Fxml/Client/CodeVerification.fxml"),
    ADMIN_VERIFICATION_CODE("/Fxml/Admin/CodeVerification.fxml"),
    CLIENT_PARENT("/Fxml/Client/Client.fxml"),
    CLIENT_DASHBOARD("/Fxml/Client/Dashboard.fxml"),
    CLIENT_DIARY("/Fxml/Client/Diary.fxml"),
    CLIENT_RECORDS("/Fxml/Client/Records.fxml"),
    ADMIN_PARENT("/Fxml/Admin/Admin.fxml"),
    ADMIN_DASHBOARD("/Fxml/Admin/Dashboard.fxml"),
    ADMIN_DIARY("/Fxml/Admin/Diary.fxml"),
    ADMIN_RECORDS("/Fxml/Admin/Records.fxml");

    private String path;

    Windows(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
