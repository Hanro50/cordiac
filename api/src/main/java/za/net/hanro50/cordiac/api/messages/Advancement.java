package za.net.hanro50.cordiac.api.messages;

import java.util.UUID;

import com.google.gson.annotations.Expose;

public class Advancement extends BaseMessage{
    @Expose
    String namespace;

    public Advancement(UUID playerId, String namespace) {
        super(playerId);
        this.namespace = namespace;
    }

    public String getNameSpace(){
        return namespace;
    }
}
