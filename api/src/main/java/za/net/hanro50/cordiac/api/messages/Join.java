package za.net.hanro50.cordiac.api.messages;

import java.util.UUID;

public class Join extends BaseMessage{

    public Join(UUID playerID) {
        super(playerID);
    }

}
