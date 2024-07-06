package me.openautonomousconnection.dns.listeners;

import me.finn.libraries.eventsystem.EventPriority;
import me.finn.libraries.eventsystem.Listener;
import me.openautonomousconnection.dns.Main;
import me.openautonomousconnection.dns.domains.DomainManager;
import me.openautonomousconnection.dns.utils.APIManager;
import me.openautonomousconnection.protocol.domain.Domain;
import me.openautonomousconnection.protocol.domain.RequestDomain;
import me.openautonomousconnection.protocol.packets.MessagePacket;
import me.openautonomousconnection.protocol.packets.PingPacket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;

public class PingListener {

    @Listener(priority = EventPriority.HIGH)
    public void onPing(PingPacket.PingPacketReceiveEvent event) {
        if (!event.isRequest) return;

        try {
            if (!APIManager.validateKey(event.apiInformation)) {
                Main.server.getClientHandlerByID(event.id).sendPacket(new MessagePacket(event.id, "Invalid api information!"));
                return;
            }
        } catch (SQLException | IOException | ClassNotFoundException exception) {
            try {
                Main.server.getClientHandlerByID(event.id).sendPacket(new MessagePacket(event.id, "Failed to validate your api information: " + exception.getMessage()));
            } catch (IOException | ClassNotFoundException exception1) {
                exception1.printStackTrace();
            }

            return;
        }

        RequestDomain requestDomain = null;
        Domain domain = null;
        boolean reachable = false;

        try {
            requestDomain = event.requestDomain;
            domain = DomainManager.getDomain(requestDomain);
            InetAddress address = null;

            reachable = domain != null;

            if (reachable) address = InetAddress.getByName(domain.parsedDestination());
            if (address == null) reachable = false;
            else reachable = address.isReachable(10000);

            try {
                Main.server.getClientHandlerByID(event.id).sendPacket(new PingPacket(event.id, false, reachable, requestDomain, domain, event.apiInformation));
            } catch (IOException | ClassNotFoundException exception) {
                exception.printStackTrace();
            }
        } catch (UnknownHostException ignored) {
            reachable = domain.parsedDestination().startsWith("https://raw.githubusercontent.com/");

            try {
                Main.server.getClientHandlerByID(event.id).sendPacket(new PingPacket(event.id, false, reachable, requestDomain, domain, event.apiInformation));
            } catch (IOException | ClassNotFoundException exc) {
                exc.printStackTrace();
            }
        } catch (SQLException | IOException exception) {
            exception.printStackTrace();
        }
    }
}
