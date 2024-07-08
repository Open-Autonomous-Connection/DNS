package me.openautonomousconnection.dns.utils;

import me.openautonomousconnection.dns.account.AccountManager;
import me.openautonomousconnection.protocol.utils.APIInformation;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class APIManager {
    public static String generateKey(String username, String password, String application) throws SQLException, InvalidAlgorithmParameterException, NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, InvalidKeySpecException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        if (hasKey(username, application)) return null;
        if (!AccountManager.validateCredentials(username, password)) return null;
        if (getKeys(username, password).size() >= Config.maxApiKeys() && Config.maxApiKeys() != -1) return null;

        String key = Utils.createAccessKey(application + username + Utils.getAlphaNumericString(5));

        PreparedStatement statement = Database.getConnection().prepareStatement("INSERT INTO apikeys (application, keyapi, username) VALUES (?, ?, ?)");
        statement.setString(1, application.toLowerCase());
        statement.setString(2, key);
        statement.setString(3, username);
        statement.executeUpdate();

        return key;
    }

    public static boolean hasKey(String username, String application) throws SQLException {
        PreparedStatement statement = Database.getConnection().prepareStatement("SELECT application, username FROM apikeys WHERE application = ? AND username = ?");
        statement.setString(1, application.toLowerCase());
        statement.setString(2, username);

        ResultSet result = statement.executeQuery();
        return result.next() && result.getString("application").equalsIgnoreCase(application) && result.getString("username").equalsIgnoreCase(username);
    }

    public static boolean validateKey(String username, String application, String apiKey) throws SQLException {
        if (!hasKey(username, application)) return false;

        PreparedStatement statement = Database.getConnection().prepareStatement("SELECT application, keyapi, username FROM apikeys WHERE application = ? AND keyapi = ? AND username = ?");
        statement.setString(1, application.toLowerCase());
        statement.setString(2, apiKey);
        statement.setString(3, username);

        ResultSet result = statement.executeQuery();
        return result.next() && result.getString("application").equalsIgnoreCase(application) && result.getString("keyapi").equals(apiKey) && result.getString("username").equalsIgnoreCase(username);
    }

    public static boolean validateKey(APIInformation apiInformation) throws SQLException {
        return validateKey(apiInformation.username, apiInformation.apiApplication, apiInformation.apiKey);
    }

    public static void deleteKey(String username, String password, String application, String apiKey) throws SQLException, InvalidAlgorithmParameterException, NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, InvalidKeySpecException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        if (!AccountManager.validateCredentials(username, password)) return;
        if (!hasKey(username, application)) return;

        PreparedStatement statement = Database.getConnection().prepareStatement("DELETE FROM apikeys WHERE application = ? AND keyapi = ? AND username = ?");
        statement.setString(1, application.toLowerCase());
        statement.setString(2, apiKey);
        statement.setString(3, username);
        statement.executeUpdate();
    }

    public static HashMap<String, String> getKeys(String username, String password) throws SQLException, InvalidAlgorithmParameterException, NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, InvalidKeySpecException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        if (!AccountManager.validateCredentials(username, password)) return null;

        HashMap<String, String> keys = new HashMap<>();

        PreparedStatement statement = Database.getConnection().prepareStatement("SELECT application, keyapi FROM apikeys WHERE username = ?");
        statement.setString(1, username);
        ResultSet result = statement.executeQuery();

        while (result.next()) keys.put(result.getString("application"), result.getString("keyapi"));
        return keys;
    }
}
