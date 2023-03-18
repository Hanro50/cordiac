package za.net.hanro50.interfaces.Data;

import java.io.File;
import java.io.IOException;

public class ChannelLinker extends Linker<String, String> {

    public ChannelLinker(File file) throws IOException {
        super(file, ChannelLinker.class);
    }

    protected ChannelLinker() {
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
