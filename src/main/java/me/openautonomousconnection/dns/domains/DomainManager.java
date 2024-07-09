/*
 * Copyright (C) 2024 Open Autonomous Connection - All Rights Reserved
 *
 * You are unauthorized to remove this copyright.
 * You have to give Credits to the Author in your project and link this GitHub site: https://github.com/Open-Autonomous-Connection
 * See LICENSE-File if exists
 */

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
import java.util.regex.Pattern;

public class DomainManager {
    public static final Pattern DOMAIN_PATTERN = Pattern.compile("^(?!-)[A-Za-z0-9-]{1,63}(?<!-)$");

    public static boolean domainExists(RequestDomain domain) throws SQLException {
        if (domain.name.equalsIgnoreCase("info")) return true;
        for (Domain registeredDomain : getDomains())
            if (registeredDomain.name.equalsIgnoreCase(domain.name) &&
                    registeredDomain.topLevelDomain.equalsIgnoreCase(domain.topLevelDomain)) return true;

        return false;
    }

    public static boolean isValidDomain(String domainName) {
        return DOMAIN_PATTERN.matcher(domainName).matches();
    }

    public static boolean domainExists(Domain domain) throws SQLException {
        return domainExists(new RequestDomain(domain.name, domain.topLevelDomain));
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
        List<Domain> domains = new ArrayList<>();

        ResultSet result = Database.getConnection().prepareStatement("SELECT name, topleveldomain, destination FROM domains").executeQuery();
        while (result.next()) {
            String name = result.getString("name");
            String topLevelDomain = result.getString("topleveldomain");
            String destination = result.getString("destination").replace("localhost", "127.0.0.1").replace("0", "127.0.0.1");
            domains.add(new Domain(name, topLevelDomain, destination));
        }

        ResultSet resultSet = Database.getConnection().prepareStatement("SELECT name, info FROM topleveldomains").executeQuery();
        while (resultSet.next()) {
            String topLevelDomain = resultSet.getString("name");
            String destination = resultSet.getString("info").replace("localhost", "127.0.0.1").replace("0", "127.0.0.1");
            domains.add(new Domain("info", topLevelDomain, destination));
        }

        domains.add(new Domain("info", "oac", Config.getInfoSite()));
        domains.add(new Domain(Config.getInterfaceSiteName(), "oac", Config.getInterfaceSiteDestination()));
        return domains;
    }
}
