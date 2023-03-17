package za.net.hanro50.interfaces.Data;

import java.util.UUID;

public class PlayerLinker extends Linker<Long, UUID> {

    public UUID getUUID(Long discordID) {
        return data.get(discordID);
    }

    public Long getDiscordID(UUID uuid) {
        return getInverse().get(uuid);
    }
}
