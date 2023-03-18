package za.net.hanro50.interfaces.Data;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PlayerLinker extends Linker<String, UUID> {

    public PlayerLinker(File file) throws IOException {
        super(file, PlayerLinker.class);
    }

    protected PlayerLinker() {
        super();
    }

    public UUID getUUID(String discordID) {
        return data.get(discordID);
    }

    public String getDiscordID(UUID uuid) {
        return getInverse().get(uuid);
    }
}
