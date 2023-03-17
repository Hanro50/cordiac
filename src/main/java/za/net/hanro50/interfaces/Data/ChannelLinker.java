package za.net.hanro50.interfaces.Data;

public class ChannelLinker extends Linker<String, String> {

    public ChannelLinker() {
        super();
    }

    public void linkServer(Channel channel, String ChannelName) {
        data.put(ChannelName, channel.getID());
    }

    public String getChannelName(Channel channel) {
        return getInverse().get(channel.getID());
    }

    public Channel getChannel(String ChannelName) {
        return Channel.parse(data.get(ChannelName));
    }

}
