package za.net.hanro50.cordiac.api;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;

import za.net.hanro50.cordiac.core.Util;

public class DPlayer {
    protected static Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();

    /** Their minecraft UUID */
    @Expose
    protected UUID uuid;
    /** Their discord ID */
    @Expose
    protected String discordID;
    /** Their roles */
    @Expose
    protected List<DRole> roles;
    /** Their discord name */
    @Expose
    protected String nickName;
    /** Their MC username name */
    protected String userName;
    /** Their name colour */
    @Expose
    protected Integer color;
    @Expose
    protected boolean linked;

    public UUID getUuid() {
        return uuid;
    }

    public String getDiscordID() {
        return discordID;
    }

    public List<DRole> getRoles() {
        return roles;
    }

    public String getUserName() {
        return userName;
    }

    public String getNickName() {
        return nickName;
    }

    public Color getColor() {
        return new Color(color);
    }

    public boolean isLinked() {
        return linked;
    }

    public DPlayer() {
    }

    protected void save(File datafile, UUID uuid) throws IOException {
        File mainFile = new File(datafile, uuid.toString() + ".json");
        Util.write(mainFile, gson.toJson(this));
    }

    public DPlayer(Bridge linker, DPlayer LNK) throws IOException {
        File root = new File(linker.getRoot(), "cache");
        this.uuid = LNK.uuid;
        this.discordID = LNK.discordID;
        this.roles = new ArrayList<>(LNK.roles);
        this.nickName = LNK.nickName;
        this.userName = linker.getUserName(uuid);
        this.color = LNK.color;
        if (linked)
            save(root, uuid);
    }

    public DPlayer(Bridge linker, UUID uuid) throws JsonSyntaxException, IOException {
        File root = new File(linker.getRoot(), "cache");
        File mainFile = new File(root, uuid.toString() + ".json");
        this.userName = linker.getUserName(uuid);
        this.uuid = uuid;
        if (mainFile.exists()) {
            DPlayer LNK = gson.fromJson(Util.readFile(mainFile), DPlayer.class);
            this.discordID = LNK.discordID;
            this.roles = new ArrayList<>(LNK.roles);
            this.nickName = LNK.nickName;
            this.color = LNK.color;
            this.linked = true;
        } else {
            this.discordID = null;
            this.roles = new ArrayList<>();
            this.nickName = userName;
            this.color = Color.white.getRGB();
            this.linked = false;
        }
    }

}
