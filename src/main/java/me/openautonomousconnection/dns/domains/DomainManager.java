package me.openautonomousconnection.dns.domains;

import me.openautonomousconnection.dns.tld.TLDManager;
import me.openautonomousconnection.dns.utils.Config;
import me.openautonomousconnection.dns.utils.Database;
import me.openautonomousconnection.protocol.domain.Domain;
import me.openautonomousconnection.protocol.domain.RequestDomain;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DomainManager {
    public static boolean domainExists(RequestDomain domain) throws SQLException {
        for (Domain registeredDomain : getDomains())
            if (registeredDomain.name.equalsIgnoreCase(domain.name) &&
                    registeredDomain.topLevelDomain.equalsIgnoreCase(domain.topLevelDomain)) return true;

        return false;
    }

    public static boolean domainExists(Domain domain) throws SQLException {
        return domainExists(new RequestDomain(domain.name, domain.topLevelDomain));
    }

    public static void createDomain(Domain domain, String accessKey) throws SQLException {
        if (domainExists(domain)) return;
        if (!TLDManager.topLevelDomainExists(domain.topLevelDomain)) return;
        if (domain.name.length() > 10) return;
        if (!Config.domainRegisteringAllowed()) return;

        PreparedStatement statement = Database.getConnection().prepareStatement("INSERT INTO domains (name, topleveldomain, destination, accesskey) VALUES (?, ?, ?, ?)");
        statement.setString(1, domain.name.toLowerCase());
        statement.setString(2, domain.topLevelDomain.toLowerCase());
        statement.setString(3, domain.realDestination());
        statement.setString(4, accessKey);
        statement.executeUpdate();
    }

    public static void deleteDomain(Domain domain, String accessKey) throws SQLException {
        if (!domainExists(domain)) return;

        PreparedStatement statement = Database.getConnection().prepareStatement("DELETE FROM domains WHERE name = ? AND topleveldomain = ? AND accesskey = ?");
        statement.setString(1, domain.name);
        statement.setString(2, domain.topLevelDomain);
        statement.setString(3, accessKey);
        statement.executeUpdate();
    }

    public static Domain getDomain(String name, String topLevelDomain) throws SQLException {
        for (Domain domain : getDomains())
            if (domain.name.equalsIgnoreCase(name) && domain.topLevelDomain.equalsIgnoreCase(topLevelDomain))
                return domain;
        return null;
    }

    public static Domain getDomain(RequestDomain requestDomain) throws SQLException {
        return getDomain(requestDomain.name, requestDomain.topLevelDomain);
    }

    public static List<Domain> getDomains() throws SQLException {
        List<Domain> tlds = new ArrayList<>();

        ResultSet result = Database.getConnection().prepareStatement("SELECT name, topleveldomain, destination FROM domains").executeQuery();
        while (result.next()) {
            String name = result.getString("name");
            String topLevelDomain = result.getString("topleveldomain");
            String destination = result.getString("destination");
            tlds.add(new Domain(name, topLevelDomain, destination));
        }

        return tlds;
    }
}
