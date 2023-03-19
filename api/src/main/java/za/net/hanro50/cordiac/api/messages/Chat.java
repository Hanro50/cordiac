package za.net.hanro50.cordiac.api.messages;

import java.util.UUID;

import com.google.gson.annotations.Expose;

public class Chat extends BaseMessage {
    @Expose
    String message;

    public Chat(UUID playerId, String message) {
        super(playerId);
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
