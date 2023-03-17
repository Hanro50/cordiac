package za.net.hanro50.interfaces.Data;

public class Channel {
    public final String guildID;
    public final String channelID;

    public Channel(String guildID, String channelID) {
        this.guildID = guildID;
        this.channelID = channelID;
    }
    public static Channel parse(String id){
        String[] data= id.split("_");
        return new Channel(data[0], data[1]);
    }

    public String getID() {
        return this.guildID + "_" + this.channelID;
    }

    @Override
    public boolean equals(Object obj) {
        System.out.println(obj.getClass().getName());
        if (obj instanceof Channel) {

            return this.getID().equals(((Channel) obj).getID());
        }
        return false;
    }
}
