package za.net.hanro50.interfaces;

public interface Handler {
    String getDiscordToken();

    String[] getTrusted();

    void addTrusted(String id);

    String getMCUsername(String UUID);

    String translate(String id);
}
