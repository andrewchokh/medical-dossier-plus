package com.andrewchokh.medicaldossierplus.Config;

import java.text.DecimalFormat;

public class Config {
    public static final String DATABASE_URL = "jdbc:sqlite:mdp.sqlite";
    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.0");
}
