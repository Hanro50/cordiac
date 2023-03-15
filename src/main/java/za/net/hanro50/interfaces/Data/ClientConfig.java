package za.net.hanro50.interfaces.Data;

public class ClientConfig {
    public final String CHANNEL;
    public final String GUILD;

    public ClientConfig(String GuildID, String CHANNELID) {
        this.CHANNEL = CHANNELID;
        this.GUILD = GuildID;
    }
}
