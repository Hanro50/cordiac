package za.net.hanro50.cordiac.api.messages;

import java.util.UUID;

import com.google.gson.annotations.Expose;


public class Death extends BaseMessage {

    public Death(UUID playerID) {
        super(playerID);
        // TODO Auto-generated constructor stub
    }

    @Expose
    private String DeathMsg;
    @Expose
    private String MobName;
    @Expose
    private UUID Attacker;
    @Expose
    private String Weapon;
@Expose 
private boolean isCustomized;

    public Death(String DeathMsg, String MobName, UUID Victem, UUID Attacker, String Weapon,boolean isCustomized) {
        super(Victem);
        this.DeathMsg = DeathMsg;
        this.MobName = MobName;
        this.Attacker = Attacker;
        this.Weapon = Weapon;
        this.isCustomized = isCustomized;
    }

 
    public String getNameSpace() {
        return DeathMsg;
    }

    public String getMobName() {
        return MobName;
    }

    public UUID getAttacker() {
        return Attacker;
    }



    public String getWeapon() {
        return Weapon;
    }


    public boolean isCustomized() {
        return isCustomized;
    }
}

  
