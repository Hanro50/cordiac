package za.net.hanro50.cordiac.bridge;

import java.io.File;
import java.io.FileOutputStream;
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
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import za.net.hanro50.cordiac.client.SpigotClient;
import za.net.hanro50.cordiac.lang.Parser;
import za.net.hanro50.cordiac.server.Discord;
import za.net.hanro50.interfaces.Handler;
import za.net.hanro50.interfaces.Server;
import za.net.hanro50.interfaces.Data.ChannelLinker;
import za.net.hanro50.interfaces.Data.PlayerLinker;
import za.net.hanro50.utils.Util;

public class SpigotPlugin extends JavaPlugin implements Handler {
    YamlConfiguration configYaml;
    String token;
    File config, data, player;
    Server server;
    ChannelLinker channelLinker;
    PlayerLinker playerLinker;
    Parser parser;

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
        OfflinePlayer plr = Bukkit.getOfflinePlayer(UUID);
        if (plr.isOnline())
            return Bukkit.getPlayer(UUID).getDisplayName();
        return plr.getName();
    }

    @Override
    public String translate(String id) {
        return id;
    }

    public void onEnable() {

        config = new File(this.getDataFolder(), "Discord.yaml");
        data = new File(this.getDataFolder(), "Channels.json");
        player = new File(this.getDataFolder(), "Players.json");
        try {
            channelLinker = new ChannelLinker(data);
            playerLinker = new PlayerLinker(player);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
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
                SpigotClient GC = new SpigotClient(server, this, "Global");
                this.getCommand("link").setExecutor(GC);
                this.getCommand("unlink").setExecutor(GC);
                this.getServer().getPluginManager().registerEvents(GC, this);

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
    public PlayerLinker getPlayerLinker() {
        // TODO Auto-generated method stub
        return playerLinker;
    }

    @Override
    public ChannelLinker getChannelLinker() {
        return channelLinker;
    }

    @Override
    public File getCache() {
        File res = new File(getDataFolder(), "cache");
        res.mkdir();
        return res;
    }

    @Override
    public Parser getLangParser() {
        if (parser == null)
            try {
                File langDir = new File(this.getDataFolder(), "lang");
                langDir.mkdir();
                String version = getServer().getVersion();
                version = version.substring(version.lastIndexOf("1."));
                version = version.substring(0, version.lastIndexOf("."));
                configYaml.addDefault("languages", new ArrayList<>());
                List<String> ltr = configYaml.getStringList("languages");
                parser = new Parser(getLogger(), version, langDir, ltr);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        return parser;
    }

}
