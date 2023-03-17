package za.net.hanro50.interfaces;


import za.net.hanro50.interfaces.Data.PlayerData;

import java.awt.Color;
import java.util.UUID;

public abstract class Client {
    protected Server server;

    public Client(Server server) {
        this.server = server;
    }

    protected final void activate() {
        server.addClient(this);
    }

    public abstract String getName();

    /** A message sent by a linked player */
    public abstract void sendPlayerMessage(Color colourhex, UUID UUID, String message);

    /** A message sent by a normal discord user */
    public abstract void sendServerMessage(Color colourhex, String DiscordUsername, String message);

    /**
     * Send player information
     * 
     * @param player
     */
    public abstract void sendPlayerInformation(PlayerData[] player, boolean refresh);

    /**
     * Tells the client to kick a player
     * 
     * @param UUID
     */
    public abstract void kick(UUID UUID, String message);

    /**
     * Tells the client to ban a player
     * 
     * @param UUID
     */
    public abstract void ban(UUID UUID, String message);

}
