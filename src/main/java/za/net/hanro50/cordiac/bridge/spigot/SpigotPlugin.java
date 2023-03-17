package za.net.hanro50.cordiac.bridge.spigot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import za.net.hanro50.cordiac.server.Discord;
import za.net.hanro50.interfaces.Handler;
import za.net.hanro50.interfaces.Server;
import za.net.hanro50.interfaces.Data.Channel;
import za.net.hanro50.interfaces.Data.ChannelLinker;
import za.net.hanro50.interfaces.Data.PlayerLinker;
import za.net.hanro50.utils.Util;

public class SpigotPlugin extends JavaPlugin implements Handler {
    YamlConfiguration configYaml;
    String token;
    File config, data, player;
    Server server;
    ChannelLinker channelLinker = new ChannelLinker();
    PlayerLinker playerLinker = new PlayerLinker();
    Gson gson = new GsonBuilder().create();

    @Override
    public String getDiscordToken() {
        return token;

    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Long> getTrusted() {
        return (List<Long>) configYaml.get("TrustedUsers");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addTrusted(Long id) {
        Set<Long> lst = new HashSet<Long>((ArrayList<Long>) configYaml.get("TrustedUsers"));
        lst.add(id);
        configYaml.set("TrustedUsers", new ArrayList<>(lst));
        try {
            configYaml.save(config);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getMCUsername(UUID UUID) {
        return Bukkit.getPlayer(UUID).getDisplayName();
    }

    @Override
    public String translate(String id) {
        return id;
    }

    public void onEnable() {

        config = new File(this.getDataFolder(), "Discord.yaml");
        data = new File(this.getDataFolder(), "Channels.json");
        player = new File(this.getDataFolder(), "Players.json");
        getLogger().info("Checking config file! [" + config.getAbsolutePath() + "]");
        if (!config.exists()) {
            try {
                this.getDataFolder().mkdirs();
                config.createNewFile();
                FileOutputStream write = new FileOutputStream(config);
                InputStream read = this.getResource("Discord.yaml");
                Util.pipe(read, write);
                write.close();
                read.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        configYaml = new YamlConfiguration();
        try {
            configYaml.load(config);

            if (configYaml.get("Mode").toString().equals("Server")) {
                token = configYaml.get("DiscordToken").toString();
                getLogger().info(Settings().size() + "");
                if (token.equals("Token Here")) {
                    getLogger().info("Please add a discord bot token and reload the plugin!");
                    return;
                }

                server = new Discord(this, getLogger());
                GlobalClient GC = new GlobalClient(server, this);
                this.getCommand("link").setExecutor(GC);
                this.getCommand("unlink").setExecutor(GC);
                this.getServer().getPluginManager().registerEvents(GC, this);
                if (data.exists()) {

                    channelLinker = gson.fromJson(Util.readFile(data), ChannelLinker.class);
                }
                linkServer(new Channel("Test", "test"), "Link");
            } else {
                getLogger().info("This mode is not supported atm.");
            }
        } catch (IOException | InvalidConfigurationException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onDisable() {
        if (server != null)
            server.stop();
    }

    @Override
    public Map<String, Boolean> Settings() {
        Map<String, Object> sets = ((MemorySection) configYaml.get("Rules")).getValues(false);
        Map<String, Boolean> res = new HashMap<>();
        for (Entry<String, Object> entry : sets.entrySet()) {
            Object val = entry.getValue();
            if (val instanceof Boolean)
                res.put(entry.getKey(), (Boolean) val);
        }

        return res;
    }

    @Override
    public void linkServer(Channel channel, String ChannelName) {
        try {
            FileWriter fs = new FileWriter(data);
            channelLinker.link(channel, ChannelName);
            fs.write(gson.toJson(channelLinker));
            fs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getChannelName(Channel channel) {
        return channelLinker.getChannelName(channel);
    }

    @Override
    public Channel getChannel(String ChannelName) {
        return channelLinker.getChannel(ChannelName);
    }

    @Override
    public void linkPlayer(long discordID, UUID uuid) {
        try {
            FileWriter fs = new FileWriter(player);
            playerLinker.link(discordID, uuid);
            fs.write(gson.toJson(playerLinker));
            fs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public UUID getUUID(long discordID) {
    return playerLinker.getUUID(discordID);
    }

    @Override
    public Long getDiscordID(UUID uuid) {
        return playerLinker.getDiscordID(uuid);
    }

}
