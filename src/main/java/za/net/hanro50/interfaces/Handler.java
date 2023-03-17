package za.net.hanro50.interfaces;

import za.net.hanro50.interfaces.Data.Channel;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface Handler {
    String getDiscordToken();

    List<Long> getTrusted();

    Map<String, Boolean> Settings();

    void addTrusted(Long id);

    String getMCUsername(UUID UUID);

    String translate(String id);

    // PlayerLinker
    void linkServer(Channel channel, String ChannelName);

    String getChannelName(Channel channel);

    Channel getChannel(String ChannelName);

    void linkPlayer(long discordID, UUID uuid);

    UUID getUUID(long discordID);

    Long getDiscordID(UUID uuid);
}
