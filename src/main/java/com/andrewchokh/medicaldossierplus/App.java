package com.andrewchokh.medicaldossierplus;

import com.andrewchokh.medicaldossierplus.Models.Model;
import com.andrewchokh.medicaldossierplus.Enums.Windows;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Model.getInstance().getViewFactory().showScene(Windows.LOGIN.getPath(), null);
    }
}
