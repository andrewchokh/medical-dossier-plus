package com.andrewchokh.medicaldossierplus;

import com.andrewchokh.medicaldossierplus.Config.Config;
import com.andrewchokh.medicaldossierplus.Database.SQLite;
import com.andrewchokh.medicaldossierplus.Models.Model;
import com.andrewchokh.medicaldossierplus.Enums.Windows;
import com.andrewchokh.medicaldossierplus.Types.User;
import com.andrewchokh.medicaldossierplus.Utils.DatabaseUtil;
import java.io.File;
import java.util.Arrays;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    public static User currentUser;

    @Override
    public void start(Stage stage) {
        Model.getInstance().getViewFactory().showScene(Windows.LOGIN, null);

        checkDatabaseFile();
    }

    private void checkDatabaseFile() {
        String[] databaseUrlSplit = Config.DATABASE_URL.split(":");
        String fileName = databaseUrlSplit[databaseUrlSplit.length - 1];

        File databaseFile = new File(fileName);

        if (!databaseFile.exists() || databaseFile.isDirectory()) {
            SQLite.connect(Config.DATABASE_URL);

            DatabaseUtil.createDatabaseStructure();
        }
        else {
            SQLite.connect(Config.DATABASE_URL);
        }
    }
}

