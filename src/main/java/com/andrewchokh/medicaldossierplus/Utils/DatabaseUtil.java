package com.andrewchokh.medicaldossierplus.Utils;

import com.andrewchokh.medicaldossierplus.Database.SQLite;
import com.andrewchokh.medicaldossierplus.Types.Builders.UserBuilder;
import com.andrewchokh.medicaldossierplus.Types.User;
import java.util.HashMap;
import java.util.List;

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
                    weight                   REAL NOT NULL,
                    pulse                    INTEGER NOT NULL,
                    systolic_blood_pressure  INTEGER NOT NULL,
                    diastolic_blood_pressure INTEGER NOT NULL,
                    body_temperature         REAL NOT NULL
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

    public static User getUserById(int id) {
        List<Object> query = SQLite.executeQuery("SELECT * FROM Users WHERE id = %d".formatted(id));

        if (query.isEmpty()) return null;

        HashMap<String, Object> userData = (HashMap<String, Object>) query.get(0);

        return new UserBuilder()
            .id(id)
            .firstName((String) userData.get("first_name"))
            .lastName((String) userData.get("last_name"))
            .email((String) userData.get("email"))
            .password((String) userData.get("password"))
            .accountType((int) userData.get("account_type"))
            .build();
    }

    public static User getUserByEmail(String email) {
        List<Object> query = SQLite.executeQuery("SELECT * FROM Users WHERE email = '%s'".formatted(email));

        if (query.isEmpty()) return null;

        HashMap<String, Object> userData = (HashMap<String, Object>) query.get(0);

        return new UserBuilder()
            .id((int) userData.get("id"))
            .firstName((String) userData.get("first_name"))
            .lastName((String) userData.get("last_name"))
            .email(email)
            .password((String) userData.get("password"))
            .accountType((int) userData.get("account_type"))
            .build();
    }
}
