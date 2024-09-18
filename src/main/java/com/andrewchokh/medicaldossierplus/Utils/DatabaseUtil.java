package com.andrewchokh.medicaldossierplus.Utils;

import com.andrewchokh.medicaldossierplus.Database.SQLite;

public class DatabaseUtil {
    public static void createDatabaseStructure() {
        String sql =
            """
                CREATE TABLE DiaryEntries (
                    id            INTEGER PRIMARY KEY
                                          UNIQUE
                                          NOT NULL,
                    user_id       INTEGER NOT NULL
                                          REFERENCES Users (id) ON DELETE CASCADE,
                    content       TEXT    NOT NULL,
                    creation_date TEXT    NOT NULL
                );
                
                CREATE TABLE Records (
                        id                       INTEGER PRIMARY KEY
                                                         UNIQUE
                                                         NOT NULL,
                        regular_user_id          INTEGER NOT NULL
                                                         REFERENCES Users (id) ON DELETE CASCADE,
                        admin_user_id            INTEGER REFERENCES Users (id) ON DELETE CASCADE
                                                         NOT NULL,
                        content                  TEXT    NOT NULL,
                        creation_date            TEXT    NOT NULL,
                        weight                   NUMERIC NOT NULL,
                        pulse                    NUMERIC NOT NULL,
                        systolic_blood_pressure  INTEGER NOT NULL,
                        diastolic_blood_pressure         NOT NULL,
                        body_temperature         NUMERIC NOT NULL
                    );
                    
                CREATE TABLE Users (
                        id           INTEGER PRIMARY KEY
                                             UNIQUE
                                             NOT NULL,
                        first_name   TEXT    NOT NULL,
                        last_name    TEXT    NOT NULL,
                        email        TEXT    NOT NULL,
                        password     TEXT    NOT NULL,
                        account_type INTEGER NOT NULL
                    );
            """;

        SQLite.executeUpdate(sql);
    }

    public static int generateRandomID() {
        return (int) System.currentTimeMillis();
    }
}
