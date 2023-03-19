package za.net.hanro50.cordiac;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import com.google.gson.JsonSyntaxException;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import za.net.hanro50.cordiac.api.Bridge;
import za.net.hanro50.cordiac.api.DPlayer;

public final class DPlayerImp extends DPlayer{
    public DPlayerImp() {
    }

    public DPlayerImp(Bridge linker, DPlayerImp LNK) throws IOException {
        super(linker,LNK);
    }

    public DPlayerImp(Bridge linker, UUID uuid) throws JsonSyntaxException, IOException {
        super(linker,uuid);
    }


    public DPlayerImp(DiscordServer linker, Member member) throws IOException {
        File root = new File(linker.bridge.getRoot(), "cache");
        this.discordID = member.getId();
        this.roles = new ArrayList<>();
        this.color = member.getColorRaw();
        if (this.color == null)
            this.color = Color.white.getRGB();
        member.getRoles().forEach(e -> this.roles.add(new DRoleImp(e)));
        this.nickName = member.getEffectiveName();
        this.uuid = linker.playerLNK.getUUID(discordID);
        if (this.uuid == null) {
            this.userName = member.getUser().getName();
            this.linked = false;
        } else {
            this.userName = linker.bridge.getUserName(uuid);
            this.linked = true;
            this.save(root, uuid);
        }
    }

    public DPlayerImp(DiscordServer linker, User member) throws IOException {
        File root = new File(linker.bridge.getRoot(), "cache");
        this.discordID = member.getId();
        this.roles = new ArrayList<>();
        this.color = Color.white.getRGB();
        this.nickName = member.getName();
        this.uuid = linker.playerLNK.getUUID(discordID);
        if (this.uuid == null) {
            this.userName = member.getName();
            this.linked = false;
        } else {
            this.userName = linker.bridge.getUserName(uuid);
            this.linked = true;
            this.save(root, uuid);
        }
    }
}
