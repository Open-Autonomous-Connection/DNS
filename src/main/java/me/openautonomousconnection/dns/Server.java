package me.openautonomousconnection.dns;

import me.openautonomousconnection.dns.domains.DomainManager;
import me.openautonomousconnection.protocol.domain.Domain;
import me.openautonomousconnection.protocol.side.ProtocolServer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Server extends ProtocolServer {
    public Server(int timeoutInSeconds) throws IOException, InterruptedException {
        super(timeoutInSeconds);
    }

    @Override
    public List<Domain> getDomains() throws SQLException {
        return DomainManager.getDomains();
    }

    @Override
    public void handleMessage(int clientID, String message) {
        System.out.println("[MESSAGE] " + clientID + ": " + message);
    }
}
