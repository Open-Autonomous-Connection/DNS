package me.openautonomousconnection.dns.listeners;

import me.finn.libraries.eventsystem.EventPriority;
import me.finn.libraries.eventsystem.Listener;
import me.openautonomousconnection.dns.Main;
import me.openautonomousconnection.dns.domains.DomainManager;
import me.openautonomousconnection.dns.utils.APIManager;
import me.openautonomousconnection.protocol.RequestType;
import me.openautonomousconnection.protocol.packets.DomainPacket;
import me.openautonomousconnection.protocol.packets.MessagePacket;

import java.io.IOException;
import java.sql.SQLException;

public class DomainListener {

    @Listener(priority = EventPriority.HIGH)
    public void onDomain(DomainPacket.DomainPacketReceiveEvent event) {
        RequestType requestType = event.requestType;

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

        if (requestType == RequestType.EXISTS) {
            try {
                Main.server.getClientHandlerByID(event.id).sendPacket(new DomainPacket(event.id, requestType, DomainManager.getDomain(event.requestDomain), event.requestDomain, event.accessKey, event.apiInformation));
            } catch (SQLException | IOException | ClassNotFoundException exception) {
                exception.printStackTrace();
                return;
            }
        }
    }

}
