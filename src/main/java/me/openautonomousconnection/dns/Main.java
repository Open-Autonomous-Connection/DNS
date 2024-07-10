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
import me.openautonomousconnection.protocol.ProtocolBridge;
import me.openautonomousconnection.protocol.ProtocolSettings;
import me.openautonomousconnection.protocol.ProtocolVersion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;

public class Main {
    public static ProtocolBridge protocolBridge;

    public static void main(String[] args) {
        try {
            URL oracle = new URL("https://raw.githubusercontent.com/Open-Autonomous-Connection/dns/master/src/resources/version.txt");

            BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));
            String version = "";
            String inputLine;
            while ((inputLine = in.readLine()) != null) version += inputLine;

            if (!version.equalsIgnoreCase(Files.readString(Path.of(Main.class.getResource("../../../version.txt").toURI())))) {
                System.out.println();
                System.out.println("===============================================");
                System.out.println("IMPORTANT: A NEW SERVER VERSION IS PUBLISHED ON GITHUB");
                System.out.println("===============================================");
                System.out.println();
            }
        } catch (IOException | URISyntaxException exception) {
            System.out.println();
            System.out.println("===============================================");
            System.out.println("IMPORTANT: SERVER VERSION CHECK COULD NOT COMPLETED! VISIT OUR GITHUB");
            System.out.println("https://github.com/Open-Autonomous-Connection");
            System.out.println("===============================================");
            System.out.println();
        }

        try {
            Config.init();
            Database.connect();
        } catch (SQLException | InstantiationException | ClassNotFoundException | IllegalAccessException | IOException exception) {
            exception.printStackTrace();
            return;
        }

        final ProtocolSettings protocolSettings = new ProtocolSettings();
        protocolSettings.port = Config.getPort();

        try {
            protocolBridge = new ProtocolBridge(ProtocolVersion.PV_1_0_0, protocolSettings, new Server());
            protocolBridge.getProtocolServer().setProtocolBridge(protocolBridge);
            protocolBridge.getProtocolServer().getServer().getEventManager().registerListener(ServerEventListener.class);
            protocolBridge.getProtocolServer().startServer();
            System.out.println();
        } catch (IOException | InterruptedException | InvocationTargetException | NoSuchMethodException |
                 InstantiationException | IllegalAccessException exception) {
            exception.printStackTrace();
            return;
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                protocolBridge.getProtocolServer().stopServer();
                Database.close();
            } catch (SQLException | IOException exception) {
                exception.printStackTrace();
            }
        }));
    }
}