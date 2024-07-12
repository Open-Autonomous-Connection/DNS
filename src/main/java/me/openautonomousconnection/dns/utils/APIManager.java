/*
 * Copyright (C) 2024 Open Autonomous Connection - All Rights Reserved
 *
 * You are unauthorized to remove this copyright.
 * You have to give Credits to the Author in your project and link this GitHub site: https://github.com/Open-Autonomous-Connection
 * See LICENSE-File if exists
 */

package me.openautonomousconnection.dns.utils;

import me.openautonomousconnection.protocol.utils.APIInformation;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class APIManager {

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
}
