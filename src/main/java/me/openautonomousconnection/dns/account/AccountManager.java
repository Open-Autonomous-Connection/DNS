package me.openautonomousconnection.dns.account;

import me.openautonomousconnection.dns.utils.APIManager;
import me.openautonomousconnection.dns.utils.Config;
import me.openautonomousconnection.dns.utils.Database;
import me.openautonomousconnection.dns.utils.Utils;

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
import java.util.Map;

public class AccountManager {

    public static void createAccount(String username, String password) throws SQLException {
        if (usernameExists(username)) return;
        if (!Config.accountRegisteringAllowed()) return;

        PreparedStatement statement = Database.getConnection().prepareStatement("INSERT INTO accounts (username, password) VALUES (?, ?)");
        statement.setString(1, username);
        statement.setString(2, Utils.sha256(password));
        statement.executeUpdate();
    }

    public static void deleteAccount(String username, String password) throws SQLException, InvalidAlgorithmParameterException, NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, InvalidKeySpecException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        if (!validateCredentials(username, password)) return;

        if (!APIManager.getKeys(username, password).isEmpty())
            for (Map.Entry<String, String> apiKeys : APIManager.getKeys(username, password).entrySet())
                APIManager.deleteKey(username, password, apiKeys.getKey(), apiKeys.getValue());

        PreparedStatement statement = Database.getConnection().prepareStatement("DELETE FROM accounts WHERE username = ? AND password = ?");
        statement.setString(1, username);
        statement.setString(2, Utils.sha256(password));
        statement.executeUpdate();
    }

    public static boolean usernameExists(String username) throws SQLException {
        PreparedStatement statement = Database.getConnection().prepareStatement("SELECT username FROM accounts WHERE username = ?");
        statement.setString(1, username);

        ResultSet result = statement.executeQuery();
        return result.next() && !username.isEmpty() && !username.isBlank() && result.getString("username").equalsIgnoreCase(username);
    }

    public static boolean validateCredentials(String username, String password) throws SQLException {
        if (!usernameExists(username) || username.isEmpty() || username.isBlank()) return false;

        password = Utils.sha256(password);

        PreparedStatement statement = Database.getConnection().prepareStatement("SELECT username, password FROM accounts WHERE username = ? AND password = ?");
        statement.setString(1, username);
        statement.setString(2, password);

        ResultSet result = statement.executeQuery();
        return result.next() && result.getString("username").equals(username) && result.getString("password").equals(password);
    }
}
