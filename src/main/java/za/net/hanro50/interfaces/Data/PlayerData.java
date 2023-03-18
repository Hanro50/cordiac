package za.net.hanro50.interfaces.Data;

import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import za.net.hanro50.interfaces.Handler;
import za.net.hanro50.utils.Util;

public final class PlayerData {

    private static Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();

    public static class DiscordRole {
        @Expose
        public Long id;
        @Expose
        public Color color;
        @Expose
        public String name;
        @Expose
        public boolean admin;
        @Expose
        public Long guildID;

        public DiscordRole() {
        }

        public DiscordRole(Role role) {
            this.id = role.getIdLong();
            this.color = role.getColor();
            this.name = role.getName();
            this.admin = role.hasPermission(Permission.ADMINISTRATOR);
            this.guildID = role.getGuild().getIdLong();
        }
    }

    /** Their minecraft UUID */
    @Expose
    public UUID uuid;
    /** Their discord ID */
    @Expose
    public String discordID;
    /** Their roles */
    @Expose
    public List<DiscordRole> roles;
    /** Their discord name */
    @Expose
    public String nickName;
    /** Their MC username name */
    public String userName;
    /** Their name colour */
    @Expose
    public Color color;
    @Expose
    public boolean linked;

    public PlayerData() {
    }

    public PlayerData(Handler linker, PlayerData LNK) {
        this.uuid = LNK.uuid;
        this.discordID = LNK.discordID;
        this.roles = new ArrayList<>(LNK.roles);
        this.nickName = LNK.nickName;
        this.userName = linker.getMCUsername(uuid);
        this.color = LNK.color;
        if (linked)
            save(linker.getCache(), uuid);
    }

    public PlayerData(Handler linker, UUID uuid) throws JsonSyntaxException, IOException {
        File mainFile = new File(linker.getCache(), uuid.toString() + ".json");
        this.userName = linker.getMCUsername(uuid);
        this.uuid = uuid;
        if (mainFile.exists()) {
            PlayerData LNK = gson.fromJson(Util.readFile(mainFile), PlayerData.class);
            this.discordID = LNK.discordID;
            this.roles = new ArrayList<>(LNK.roles);
            this.nickName = LNK.nickName;
            this.color = LNK.color;
            this.linked = true;
        } else {
            this.discordID = null;
            this.roles = new ArrayList<>();
            this.nickName = userName;
            this.color = Color.white;
            this.linked = false;
        }
    }

    public PlayerData(Handler linker, Member member) {
        this.discordID = member.getId();
        this.roles = new ArrayList<>();
        this.color = member.getColor();
        if (this.color == null)
            this.color = Color.white;
        member.getRoles().forEach(e -> this.roles.add(new DiscordRole(e)));
        this.nickName = member.getEffectiveName();
        this.uuid = linker.getPlayerLinker().getUUID(discordID);
        if (this.uuid == null) {
            this.userName = member.getUser().getName();
            this.linked = false;
        } else {
            this.userName = linker.getMCUsername(uuid);
            this.linked = true;
            this.save(linker.getCache(), uuid);
        }
    }

    public PlayerData(Handler linker, User member) {
        this.discordID = member.getId();
        this.roles = new ArrayList<>();
        this.color = Color.white;
        this.nickName = member.getName();
        this.uuid = linker.getPlayerLinker().getUUID(discordID);
        if (this.uuid == null) {
            this.userName = member.getName();
            this.linked = false;
        } else {
            this.userName = linker.getMCUsername(uuid);
            this.linked = true;
            this.save(linker.getCache(), uuid);
        }
    }

    private void save(File datafile, UUID uuid) {
        File mainFile = new File(datafile, uuid.toString() + ".json");
        try {
            if (!mainFile.exists()) {
                mainFile.createNewFile();
            }
            FileWriter fs = new FileWriter(mainFile);
            fs.write(gson.toJson(this));
            fs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
