package za.net.hanro50.cordiac.api.messages;

import java.util.UUID;

import com.google.gson.annotations.Expose;

public class BaseMessage {
    @Expose
    UUID playerID;

    public UUID getPlayerID() {
        return playerID;
    }

    public BaseMessage(UUID playerID) {
        this.playerID = playerID;
    }

}
