package za.net.hanro50.cordiac.api.messages;

import java.util.UUID;

import com.google.gson.annotations.Expose;

public class Achievement extends BaseMessage {
    @Expose
    String namespace;

    public Achievement(UUID playerId, String namespace) {
        super(playerId);
        this.namespace = namespace;
    }

    public String getNameSpace(){
        return namespace;
    }
}
