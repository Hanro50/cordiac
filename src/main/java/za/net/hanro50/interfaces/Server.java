package za.net.hanro50.interfaces;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import za.net.hanro50.interfaces.Data.Cause;

public abstract class Server {
    private Map<String, Client> Clients = new HashMap<>();

    final void addClient(Client client) {
        this.Clients.put(client.getName(), client);
    }

    public final Map<String, Client> getClients() {
        return Collections.unmodifiableMap(Clients);
    }

    final void removeClient(Client client) {
        this.Clients.remove(client.getName());
    }

    public abstract void playerJoin(Client client, UUID UUID);

    public abstract void playerLeave(Client client, UUID UUID);

    /**
     * The client generates a code and sends it to the server.
     * On discord a user will then input a command along with the code in the bot's
     * DMs
     * This will link their Discord and Player accounts.
     * 
     * @param UUID
     * @param code
     */
    public abstract void sendLinkRequest(UUID UUID, String code);

    public abstract void sendUnLinkRequest(UUID UUID);

    public abstract void sendMessage(Client client, UUID UUID, String message);

    public abstract void sendAdvancement(Client client, String AdvancementId, UUID playerUUID);

    public abstract void sendDeathMessage(Client client, String DeathId, UUID playerUUID, Cause cause);

    public abstract void requestData(Client client);

    public abstract void stop();

    public void sendDeathMessage(Client client, String DeathId, UUID playerUUID) {
        sendDeathMessage(client, DeathId, playerUUID, null);
    }
}
