package za.net.hanro50.interfaces;

import za.net.hanro50.cordiac.lang.Parser;
import za.net.hanro50.interfaces.Data.ChannelLinker;
import za.net.hanro50.interfaces.Data.PlayerLinker;

import java.io.File;
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
    PlayerLinker getPlayerLinker();

    ChannelLinker getChannelLinker();

    File getCache();

    Parser getLangParser();

}
