package me.openautonomousconnection.dns;

import me.finn.libraries.eventsystem.EventManager;
import me.finn.libraries.networksystem.packets.PacketHandler;
import me.finn.libraries.networksystem.server.NetworkServer;
import me.openautonomousconnection.dns.listeners.DomainListener;
import me.openautonomousconnection.dns.listeners.MessageListener;
import me.openautonomousconnection.dns.listeners.PingListener;
import me.openautonomousconnection.dns.listeners.TopLevelDomainListener;
import me.openautonomousconnection.dns.utils.Database;
import me.openautonomousconnection.protocol.packets.DomainPacket;
import me.openautonomousconnection.protocol.packets.MessagePacket;
import me.openautonomousconnection.protocol.packets.PingPacket;
import me.openautonomousconnection.protocol.packets.TopLevelDomainPacket;

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
    public static NetworkServer server;

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
                System.out.println("IMPORTANT: A NEW VERSION IS PUBLISHED ON GITHUB");
                System.out.println("===============================================");
                System.out.println();
            }
        } catch (IOException | URISyntaxException exception) {
            System.out.println();
            System.out.println("===============================================");
            System.out.println("IMPORTANT: VERSION CHECK COULD NOT COMPLETED! VISIT OUR GITHUB");
            System.out.println("https://github.com/Open-Autonomous-Connection");
            System.out.println("===============================================");
            System.out.println();
        }

        int port = 9382;

        final PacketHandler packetHandler = new PacketHandler();

        try {
            packetHandler.registerPacket(DomainPacket.class);
            packetHandler.registerPacket(TopLevelDomainPacket.class);
            packetHandler.registerPacket(PingPacket.class);
            packetHandler.registerPacket(MessagePacket.class);
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException |
                 InstantiationException exception) {
            exception.printStackTrace();
            return;
        }

        EventManager.registerListener(new DomainListener());
        EventManager.registerListener(new MessageListener());
        EventManager.registerListener(new PingListener());
        EventManager.registerListener(new TopLevelDomainListener());

        if (args.length > 1) if (args[0].equals("--port")) port = Integer.parseInt(args[1]);

        server = new NetworkServer.ServerBuilder().
                setPort(port).setPacketHandler(packetHandler).
                enableAutoRestart().enableDebugLog().
                build();

        try {
            Database.connect();
        } catch (SQLException | InstantiationException | ClassNotFoundException | IllegalAccessException exception) {
            exception.printStackTrace();
            return;
        }

        try {
            server.start();
        } catch (IOException | InterruptedException exception) {
            exception.printStackTrace();
            return;
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                server.stop();
                Database.close();
            } catch (SQLException | IOException exception) {
                exception.printStackTrace();
            }
        }));
    }
}