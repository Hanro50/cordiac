package za.net.hanro50.cordiac.bridge;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import za.net.hanro50.interfaces.Handler;
import za.net.hanro50.utils.Util;

public class SpigotPlugin extends JavaPlugin implements Handler {
    YamlConfiguration configYaml;

    @Override
    public String getDiscordToken() {
        return null;

    }

    @Override
    public String[] getTrusted() {
        return new String[0];
    }

    @Override
    public void addTrusted(String id) {

    }

    @Override
    public String getMCUsername(String UUID) {
        return UUID;
    }

    @Override
    public String translate(String id) {
        return id;
    }

    public void onEnable() {
        File config = new File(this.getDataFolder(), "Discord.yaml");
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
            String token = configYaml.get("DiscordToken").toString();
            if (token.equals("Token Here")) {
                getLogger().info("Please add a discord bot token and reload the plugin!");
                return;
            }
        } else {
            getLogger().info("This mode is not supported atm.");

        }
    }

    public void onDisable() {

    }
}
