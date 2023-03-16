package za.net.hanro50.interfaces;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import za.net.hanro50.interfaces.Data.Cause;

public abstract class Server {
    private List<Client> Clients = new ArrayList<>();

    
    final void addClient(Client client) {
        this.Clients.add(client);
    }

    public final List<Client> getClients() {
        return Collections.unmodifiableList(Clients);
    }

    final void removeClient(Client client) {
        this.Clients.remove(client);
    }

    /**
     * The client generates a code and sends it to the server.
     * On discord a user will then input a command along with the code in the bot's
     * DMs
     * This will link their Discord and Player accounts.
     * 
     * @param UUID
     * @param code
     */
    public abstract void sendLinkRequest(String UUID, String code);

    public abstract void sendMessage(Client client, String message);

    public abstract void playerJoin(Client client, String UUID);

    public abstract void playerLeave(Client client, String UUID);

    public abstract void sendAdvancement(Client client, String AdvancementId, String playerUUID);

    public abstract void requestData(Client client);

    public abstract void getPlayerInformation(Client client, String[] UUIDs);

    public abstract void stop();

    public abstract void sendDeathMessage(Client client, String DeathId, String playerUUID, Cause cause);

    public void sendDeathMessage(Client client, String DeathId, String playerUUID) {
        sendDeathMessage(client, DeathId, playerUUID, null);
    }
}
