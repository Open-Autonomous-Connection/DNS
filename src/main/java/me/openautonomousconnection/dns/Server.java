/*
 * Copyright (C) 2024 Open Autonomous Connection - All Rights Reserved
 *
 * You are unauthorized to remove this copyright.
 * You have to give Credits to the Author in your project and link this GitHub site: https://github.com/Open-Autonomous-Connection
 * See LICENSE-File if exists
 */

package me.openautonomousconnection.dns;

import me.openautonomousconnection.dns.utils.Config;
import me.openautonomousconnection.dns.utils.Database;
import me.openautonomousconnection.protocol.domain.Domain;
import me.openautonomousconnection.protocol.side.ProtocolServer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Server extends ProtocolServer {
    @Override
    public List<Domain> getDomains() throws SQLException {
        return DomainManager.getDomains();
    }

    @Override
    public List<String> getTopLevelDomains() throws SQLException {
        return DomainManager.getTopLevelDomains();
    }

    @Override
    public String getInfoSite(String topLevelDomain) throws SQLException {
        if (!topLevelDomainExists(topLevelDomain)) return null;

        ResultSet resultSet = Database.getConnection().prepareStatement("SELECT name, info FROM topleveldomains").executeQuery();
        while (resultSet.next()) if (resultSet.getString("name").equals(topLevelDomain)) return resultSet.getString("info");

        return null;
    }

    @Override
    public String getInterfaceSite() {
        return Config.getInterfaceSite();
    }

    @Override
    public String getDNSServerInfoSite() {
        return Config.getInfoSite();
    }

    @Override
    public void handleMessage(int clientID, String message) {
        System.out.println("[MESSAGE] " + clientID + ": " + message);
    }
}
