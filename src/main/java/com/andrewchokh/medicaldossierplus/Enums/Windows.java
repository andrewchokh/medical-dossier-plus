package com.andrewchokh.medicaldossierplus.Enums;

public enum Windows {
    LOGIN("/Fxml/Login.fxml"),
    SIGNUP("/Fxml/Signup.fxml"),
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
