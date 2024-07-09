/*
 * Copyright (C) 2024 Open Autonomous Connection - All Rights Reserved
 *
 * You are unauthorized to remove this copyright.
 * You have to give Credits to the Author in your project and link this GitHub site: https://github.com/Open-Autonomous-Connection
 * See LICENSE-File if exists
 */

package me.openautonomousconnection.dns.tld;

import me.openautonomousconnection.dns.utils.Config;
import me.openautonomousconnection.dns.utils.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class TLDManager {
    public static List<String> getTopLevelDomains() throws SQLException {
        List<String> tlds = new ArrayList<>();

        ResultSet result = Database.getConnection().prepareStatement("SELECT name FROM topleveldomains").executeQuery();
        while (result.next()) tlds.add(result.getString("name"));
        return tlds;
    }
}
