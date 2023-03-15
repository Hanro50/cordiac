package za.net.hanro50.interfaces.Data;

public class Cause {
    public final boolean ISPLAYER;
    public final String ID;
    public final String WEAPON;

    public Cause(String MobID) {
        this(MobID, false);
    }

    public Cause(String UUIDorMobID, boolean player) {
        this(UUIDorMobID, player, null);
    }

    public Cause(String UUIDorMobID, boolean player, String weapon) {
        this.ID = UUIDorMobID;
        this.ISPLAYER = player;
        this.WEAPON = weapon;
    }
}
