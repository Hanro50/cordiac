package za.net.hanro50.cordiac.api;

public interface Client {
    default void activate(Server server) {
        server.addClient(this);
    }

    /** Sends a message to the broadcast channel */
    void message(String message);

    /** Sends a message as a player */
    void message(DPlayer player, String message);

    /** Kicks a set player */
    void kick(DPlayer player, String reason);

    /** Bans a set player */
    void ban(DPlayer player, String reason);

    /** Updates the local cache of a player */
    void info(DPlayer player);

    /** The ID that identifies this client. Should be unique */
    String getID();
}
