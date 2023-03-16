package za.net.hanro50.interfaces;

import za.net.hanro50.interfaces.Data.ClientConfig;
import za.net.hanro50.interfaces.Data.Player;

public abstract class Client {
    protected Server server;

    public Client(Server server) {
        this.server = server;
        server.addClient(this);
    }

    /** Used to Identify Client. Internally used to match client with server */
    public abstract ClientConfig getConfig();

    /** Used to Identify Client. Internally used to match client with server */
    public abstract void setChannelID(String string);

    /** A message sent by a linked player */
    public abstract void sendPlayerMessage(String colour, String UUID, String message);

    /** A message sent by a normal discord user */
    public abstract void sendServerMessage(String colour, String DiscordUsername, String message);

    /**
     * Sends discord data to clients to store locally.
     */
    public abstract void sendData(ClientConfig config);

    /**
     * Send player information
     * 
     * @param player
     */
    public abstract void sendPlayerInformation(Player[] player, boolean refresh);

    /**
     * Tells the client to kick a player
     * 
     * @param UUID
     */
    public abstract void kick(String UUID, String message);

    /**
     * Tells the client to ban a player
     * 
     * @param UUID
     */
    public abstract void ban(String UUID, String message);
}
