package com.andrewchokh.medicaldossierplus.Utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javafx.scene.control.Label;

public class AuthorizationUtil {
    public static String hashPassword(String rawPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawPassword.getBytes(StandardCharsets.UTF_8));
            return new String(hash, StandardCharsets.UTF_8);
        }
        catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static void changeLabelText(Label label, String text) {
        label.setVisible(!text.isEmpty());
        label.setText(text);
    }
}
