package za.net.hanro50.cordiac.api.messages;

import java.util.UUID;

import com.google.gson.annotations.Expose;

public class Leave extends BaseMessage {
    @Expose
    LeaveType type;
    String reason;

    public LeaveType getType() {
        return type;
    }

    public String getReason() {
        return reason;
    }

    public Leave(UUID playerID, LeaveType type, String reason) {
        super(playerID);
        this.type = type;
        this.reason = reason;
    }

    public static enum LeaveType {
        KICK("commands.kick.success"),
        BAN("commands.ban.success"),
        DISCONNECT("multiplayer.player.left");

        public final String code;

        LeaveType(String code) {
            this.code = code;
        }
    }
}
