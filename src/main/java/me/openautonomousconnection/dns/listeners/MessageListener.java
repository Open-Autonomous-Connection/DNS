package me.openautonomousconnection.dns.listeners;

import me.finn.libraries.eventsystem.EventPriority;
import me.finn.libraries.eventsystem.Listener;
import me.openautonomousconnection.protocol.packets.MessagePacket;

public class MessageListener {

    @Listener(priority = EventPriority.HIGH)
    public void onMessage(MessagePacket.MessagePacketReceiveEvent event) {
        System.out.println(event.message);
    }

}
