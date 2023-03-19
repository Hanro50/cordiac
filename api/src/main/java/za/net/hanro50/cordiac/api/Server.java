package za.net.hanro50.cordiac.api;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import za.net.hanro50.cordiac.api.messages.Achievement;
import za.net.hanro50.cordiac.api.messages.Advancement;
import za.net.hanro50.cordiac.api.messages.Chat;
import za.net.hanro50.cordiac.api.messages.Death;
import za.net.hanro50.cordiac.api.messages.Join;
import za.net.hanro50.cordiac.api.messages.Leave;
import za.net.hanro50.cordiac.api.messages.Link;

public abstract class Server  {
    private Map<String, Client> Clients = new HashMap<>();

    final void addClient(Client client) {
        this.Clients.put(client.getID(), client);
    }

    public final Map<String, Client> getClients() {
        return Collections.unmodifiableMap(Clients);
    }

    final void removeClient(Client client) {
        this.Clients.remove(client.getID());
    }

    public abstract boolean active();



    /**
     * The client generates a code and sends it to the server.
     * On discord a user will then input a command along with the code in the bot's
     * DMs
     * This will link their Discord and Player accounts.
     * 
     * @param UUID
     * @param code
     */
    public abstract void sendLinkRequest(Link link);

    public abstract void sendUnLinkRequest(UUID UUID);
    public abstract void sendAdvancement(Client client,Advancement advancement);
    public abstract void sendAchievement(Client client,Achievement advancement);
    public abstract void sendChat(Client client, Chat chat);
    public abstract void sendDeath(Client client,Death death);
    public abstract void sendJoin(Client client, Join join);
    public abstract void sendLeave(Client client, Leave leave);
    public abstract void requestData(Client client);

    public abstract void stop();


}
