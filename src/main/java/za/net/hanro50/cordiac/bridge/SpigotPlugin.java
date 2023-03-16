package za.net.hanro50.cordiac.bridge;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import za.net.hanro50.cordiac.server.Discord;
import za.net.hanro50.interfaces.Handler;
import za.net.hanro50.interfaces.Server;
import za.net.hanro50.utils.Util;

public class SpigotPlugin extends JavaPlugin implements Handler {
    YamlConfiguration configYaml;
    String token;
    File config;
    Server server;

    @Override
    public String getDiscordToken() {
        return token;

    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getTrusted() {
        return (List<String>) configYaml.get("TrustedUsers");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addTrusted(String id) {
        List<String> lst = (List<String>) configYaml.get("TrustedUsers");
        lst.add(id);
        configYaml.set(id, lst);
        try {
            configYaml.save(config);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getMCUsername(String UUID) {
        return Bukkit.getPlayer(UUID).getDisplayName();
    }

    @Override
    public String translate(String id) {
        return id;
    }

    public void onEnable() {
        config = new File(this.getDataFolder(), "Discord.yaml");
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
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        if (configYaml.get("Mode").toString().equals("Server")) {
            token = configYaml.get("DiscordToken").toString();
            getLogger().info(Settings().size()+"");
            if (token.equals("Token Here")) {
                getLogger().info("Please add a discord bot token and reload the plugin!");
                return;
            }

            server = new Discord(this);
        } else {
            getLogger().info("This mode is not supported atm.");
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
}
