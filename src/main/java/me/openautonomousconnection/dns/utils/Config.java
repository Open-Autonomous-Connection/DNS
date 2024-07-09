/*
 * Copyright (C) 2024 Open Autonomous Connection - All Rights Reserved
 *
 * You are unauthorized to remove this copyright.
 * You have to give Credits to the Author in your project and link this GitHub site: https://github.com/Open-Autonomous-Connection
 * See LICENSE-File if exists
 */

package me.openautonomousconnection.dns.utils;

import me.finn.unlegitlibrary.file.ConfigurationManager;

import java.io.File;
import java.io.IOException;
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

    private static File configFile = new File("./config.txt");
    private static ConfigurationManager config;
    public static void init() throws IOException {
        if (!configFile.exists()) configFile.createNewFile();
        config = new ConfigurationManager(configFile);
        config.loadProperties();

        if (!config.isSet("port")) config.set("port", 9382);
        if (!config.isSet("ping_time_seconds")) config.set("ping_time_seconds", 10);
        if (!config.isSet("sites.info")) config.set("info_site", "DNS SERVER NEED A INFO SITE!");
        if (!config.isSet("sites.interface.name")) config.set("sites.interface.name", "ONLY NAME, NO TOP LEVEL DOMAIN!");
        if (!config.isSet("sites.interface.destination")) config.set("sites.interface.destination", "127.0.0.1");
        if (!config.isSet("database.host")) config.set("database.host", "127.0.0.1");
        if (!config.isSet("database.port")) config.set("database.port", 3306);
        if (!config.isSet("database.name")) config.set("database.name", "open_autonomous_connection");
        if (!config.isSet("database.username")) config.set("database.username", "my_username");
        if (!config.isSet("database.password")) config.set("database.password", "my_password");

        config.saveProperties();
    }

    public static String getInfoSite() {
        return config.getString("sites.info").replace("localhost", "127.0.0.1").replace("0", "127.0.0.1");
    }

    public static String getInterfaceSiteName() {
        return config.getString("sites.interface.name");
    }

    public static String getInterfaceSiteDestination() {
        return config.getString("sites.interface.destination").replace("localhost", "127.0.0.1").replace("0", "127.0.0.1");
    }

    public static int getPort() {
        return config.getInt("port");
    }

    public static int getPingTimeSeconds() {
        return config.getInt("ping_time_seconds");
    }

    public static String getDatabaseHost() {
        return config.getString("database.host").replace("localhost", "127.0.0.1").replace("0", "127.0.0.1");
    }

    public static int getDatabasePort() {
        return config.getInt("database.port");
    }

    public static String getDatabaseName() {
        return config.getString("database.name");
    }

    public static String getDatabaseUsername() {
        return config.getString("database.username");
    }

    public static String getDatabasePassword() {
        return config.getString("database.password");
    }
}
