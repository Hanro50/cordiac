package za.net.hanro50.cordiac.linkers;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PlayerLNK extends Linker<String, UUID> {

    public PlayerLNK(File file) throws IOException {
        super(file, PlayerLNK.class);
    }

    protected PlayerLNK() {
        super();
    }

    public UUID getUUID(String discordID) {
        return data.get(discordID);
    }

    public String getDiscordID(UUID uuid) {
        return getInverse().get(uuid);
    }
}
