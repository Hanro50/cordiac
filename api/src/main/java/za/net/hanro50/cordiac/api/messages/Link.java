package za.net.hanro50.cordiac.api.messages;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import com.google.gson.annotations.Expose;

public final class Link extends BaseMessage {
    @Expose
    private String code;

    public String getCode(){
        return code;
    }
    public Link(UUID playerID) {
        super(playerID);
        code = String.format("%03d-%03d-%03d", ThreadLocalRandom.current().nextInt(0, 1000),
                ThreadLocalRandom.current().nextInt(0, 1000), ThreadLocalRandom.current().nextInt(0, 1000));

    }

}
