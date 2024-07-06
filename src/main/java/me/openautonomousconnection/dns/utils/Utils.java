package me.openautonomousconnection.dns.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Utils {
    public static String DATABASE_HOST = "127.0.0.1:3306";
    public static String DATABASE_NAME = "open_autonomous_connection";
    public static String DATABASE_USERNAME = "root";
    public static String DATABASE_PASSWORD = "AbgeNickt!312123";

    public static String createAccessKey(String input) {
        return sha256(shuffleString(sha256(input) + getAlphaNumericString(5) +
                sha256(getAlphaNumericString(5)) +
                sha256(getAlphaNumericString(5)) +
                sha256(getAlphaNumericString(5))));
    }

    public static String sha256(final String base) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            final byte[] hash = digest.digest(base.getBytes(StandardCharsets.UTF_8));
            final StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                final String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String getAlphaNumericString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz";
        StringBuilder builder = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = (int) (chars.length() * Math.random());
            builder.append(chars.charAt(index));
        }

        return builder.toString();
    }

    public static String shuffleString(String input) {
        List<Character> characters = new ArrayList<>();
        for (char c : input.toCharArray()) characters.add(c);

        Collections.shuffle(characters);
        StringBuilder shuffledString = new StringBuilder();

        for (char c : characters) shuffledString.append(c);
        return shuffledString.toString();
    }
}
