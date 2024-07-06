package me.openautonomousconnection.dns.tld;

import me.openautonomousconnection.dns.utils.Config;
import me.openautonomousconnection.dns.utils.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TLDManager {
    public static boolean topLevelDomainExists(String topLevelDomain) throws SQLException {
        return getTopLevelDomains().contains(topLevelDomain);
    }

    public static void createTopLevelDomain(String topLevelDomain, String accessKey) throws SQLException {
        if (topLevelDomainExists(topLevelDomain)) return;
        if (topLevelDomain.length() > 10) return;
        if (!Config.topLevelDomainRegisteringAllowed()) return;

        PreparedStatement statement = Database.getConnection().prepareStatement("INSERT INTO topleveldomains (name, accesskey) VALUES (?, ?)");
        statement.setString(1, topLevelDomain.toLowerCase());
        statement.setString(2, accessKey);
        statement.executeUpdate();
    }

    public static void deleteTopLevelDomain(String topLevelDomain, String accessKey) throws SQLException {
        if (!TLDManager.topLevelDomainExists(topLevelDomain)) return;

        PreparedStatement statement = Database.getConnection().prepareStatement("DELETE FROM topleveldomains WHERE name = ? AND accesskey = ?");
        statement.setString(1, topLevelDomain);
        statement.setString(2, accessKey);
        statement.executeUpdate();
    }

    public static List<String> getTopLevelDomains() throws SQLException {
        List<String> tlds = new ArrayList<>();

        ResultSet result = Database.getConnection().prepareStatement("SELECT name FROM topleveldomains").executeQuery();
        while (result.next()) tlds.add(result.getString("name"));
        return tlds;
    }
}
