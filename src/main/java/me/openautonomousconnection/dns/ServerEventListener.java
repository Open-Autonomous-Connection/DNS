/*
 * Copyright (C) 2024 Open Autonomous Connection - All Rights Reserved
 *
 * You are unauthorized to remove this copyright.
 * You have to give Credits to the Author in your project and link this GitHub site: https://github.com/Open-Autonomous-Connection
 * See LICENSE-File if exists
 */

package me.openautonomousconnection.dns;

import me.finn.unlegitlibrary.event.EventListener;
import me.finn.unlegitlibrary.event.Listener;
import me.finn.unlegitlibrary.network.system.server.events.client.state.S_ClientConnectedEvent;
import me.finn.unlegitlibrary.network.system.server.events.client.state.S_ClientDisconnectedEvent;
import me.openautonomousconnection.protocol.events.v1_0_0.DomainPacketReceivedEvent;
import me.openautonomousconnection.protocol.events.v1_0_0.PingPacketReceivedEvent;

import java.util.Formatter;

public class ServerEventListener extends EventListener {
    @Listener
    public void onConnect(S_ClientConnectedEvent event) {
        System.out.println("New client connected. ID: " + event.clientHandler.getClientID());
        System.out.println();
    }

    @Listener
    public void onDisconnect(S_ClientDisconnectedEvent event) {
        System.out.println("Client disconnected. ID: " + event.clientHandler.getClientID());
        System.out.println();
    }

    @Listener
    public void onPing(PingPacketReceivedEvent event) {
        System.out.println("New Ping request:");
        System.out.println("   » From client id: " + event.clientID);
        System.out.println("   » Request domain: " + event.requestDomain.toString());
        System.out.println("   » Path: " + event.requestDomain.getPath());
        System.out.println("   » Reachable: " + (event.reachable ? "Yes" : "No"));
        System.out.println("   » Destination: " + (event.domain == null ? "N/A" : event.domain.parsedDestination()));
        System.out.println();
    }

    @Listener
    public void onExistCheck(DomainPacketReceivedEvent event) {
        System.out.println("New Domain packet request:");
        System.out.println("   » From client id: " + event.clientID);
        System.out.println("   » Request domain: " + event.requestDomain.toString());
        System.out.println("   » Path: " + event.requestDomain.getPath());
        System.out.println("   » Exists: " + (event.domain == null ? "No" : "Yes"));
        System.out.println("   » Destination: " + (event.domain == null ? "N/A" : event.domain.parsedDestination()));
        System.out.println(" ");
    }
}
