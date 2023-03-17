package za.net.hanro50.interfaces.Data;

public class ChannelLinker extends Linker<String, String> {

    public void link(Channel channel, String ChannelName) {
        super.link(ChannelName, channel.getID());
    }

    public String getChannelName(Channel channel) {
        return getInverse().get(channel.getID());
    }

    public Channel getChannel(String ChannelName) {
        return Channel.parse(data.get(ChannelName));
    }

}
