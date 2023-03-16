package za.net.hanro50.interfaces;

import java.util.List;
import java.util.Map;

public interface Handler {
    String getDiscordToken();

    List<String> getTrusted();

    Map<String, Boolean> Settings();

    void addTrusted(String id);

    String getMCUsername(String UUID);

    String translate(String id);
}
