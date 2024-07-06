package me.openautonomousconnection.dns.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private static Connection connection;

    public static Connection getConnection() {
        return connection;
    }

    public static void connect() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        if (isConnected()) return;

        Class.forName("com.mysql.jdbc.Driver").newInstance();
        connection = DriverManager.getConnection("jdbc:mysql://" + Utils.DATABASE_HOST + "/" + Utils.DATABASE_NAME + "?autoReconnect=true",
                Utils.DATABASE_USERNAME, Utils.DATABASE_PASSWORD);
    }

    public static void close() throws SQLException {
        if (!isConnected()) return;

        connection.close();
        connection = null;
    }

    public static boolean isConnected() {
        return connection != null;
    }
}
