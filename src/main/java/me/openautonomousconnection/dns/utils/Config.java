package me.openautonomousconnection.dns.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Config {

    public static boolean parseBoolean(int i) {
        return i != 0;
    }

    public static int booleanToInt(boolean bool) {
        return bool ? 1 : 0;
    }

    public static boolean topLevelDomainRegisteringAllowed() throws SQLException {
        PreparedStatement statement = Database.getConnection().prepareStatement("SELECT value FROM config WHERE name = ?");
        statement.setString(1, "allow_register_tld");
        ResultSet result = statement.executeQuery();
        return result.next() && parseBoolean(Integer.parseInt(result.getString("value")));
    }

    public static boolean domainRegisteringAllowed() throws SQLException {
        PreparedStatement statement = Database.getConnection().prepareStatement("SELECT value FROM config WHERE name = ?");
        statement.setString(1, "allow_register_domain");
        ResultSet result = statement.executeQuery();
        return result.next() && parseBoolean(Integer.parseInt(result.getString("value")));
    }

    public static boolean accountRegisteringAllowed() throws SQLException {
        PreparedStatement statement = Database.getConnection().prepareStatement("SELECT value FROM config WHERE name = ?");
        statement.setString(1, "allow_register_account");
        ResultSet result = statement.executeQuery();
        return result.next() && parseBoolean(Integer.parseInt(result.getString("value")));
    }

    public static int maxApiKeys() throws SQLException {
        PreparedStatement statement = Database.getConnection().prepareStatement("SELECT value FROM config WHERE name = ?");
        statement.setString(1, "max_apikeys");
        ResultSet result = statement.executeQuery();
        if (!result.next()) return 0;
        return Integer.parseInt(result.getString("value")); // -1 = Endless
    }
}
