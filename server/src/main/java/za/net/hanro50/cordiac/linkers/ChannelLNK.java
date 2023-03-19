package za.net.hanro50.cordiac.linkers;

import java.io.File;
import java.io.IOException;

import za.net.hanro50.cordiac.core.data.Channel;

public class ChannelLNK extends Linker<String, String> {

    public ChannelLNK(File file) throws IOException {
        super(file, ChannelLNK.class);
    }

    protected ChannelLNK() {
        super();
    }

    public void link(Channel channel, String ChannelName) throws IOException {
        super.link(ChannelName, channel.getID());
    }

    public String getChannelName(Channel channel) {
        return getInverse().get(channel.getID());
    }

    public Channel getChannel(String ChannelName) {
        return Channel.parse(data.get(ChannelName));
    }

}
