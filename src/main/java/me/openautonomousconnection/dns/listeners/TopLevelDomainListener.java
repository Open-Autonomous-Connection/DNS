package me.openautonomousconnection.dns.listeners;

import me.finn.libraries.eventsystem.EventPriority;
import me.finn.libraries.eventsystem.Listener;
import me.openautonomousconnection.dns.Main;
import me.openautonomousconnection.dns.tld.TLDManager;
import me.openautonomousconnection.dns.utils.APIManager;
import me.openautonomousconnection.protocol.RequestType;
import me.openautonomousconnection.protocol.packets.MessagePacket;
import me.openautonomousconnection.protocol.packets.TopLevelDomainPacket;

import java.io.IOException;
import java.sql.SQLException;

public class TopLevelDomainListener {

    @Listener(priority = EventPriority.HIGH)
    public void onTopLevelDomain(TopLevelDomainPacket.TopLevelDomainPacketReceiveEvent event) throws IOException, ClassNotFoundException {
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
                Main.server.getClientHandlerByID(event.id).sendPacket(new TopLevelDomainPacket(event.id, requestType, event.topLevelDomain, TLDManager.topLevelDomainExists(event.topLevelDomain) ? event.topLevelDomain : null, event.accessKey, event.apiInformation));
            } catch (SQLException | IOException | ClassNotFoundException exception) {
                exception.printStackTrace();
            }
        }
    }

}
