package za.net.hanro50.cordiac;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.util.UUID;

import org.bukkit.plugin.java.JavaPlugin;

import za.net.hanro50.cordiac.api.Bridge;
import za.net.hanro50.cordiac.api.Settings;

public class Main extends JavaPlugin implements Settings, Bridge {
    DiscordServer server;

    @Override
    public void onEnable() {
        try {
            server = new DiscordServer(this, getConfig().getString("format", "Token here"));
            if (!server.active())
                return;

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        if (server != null)
            server.jda.shutdown();
    }

    @Override
    public boolean useWebHooks() {
        return getConfig().getBoolean("useWebHooks", true);
    }

    @Override
    public boolean showJoinMessages() {
        return getConfig().getBoolean("showJoinMessages", true);
    }

    @Override
    public boolean showLeaveMessages() {
        return getConfig().getBoolean("showLeaveMessages", true);
    }

    @Override
    public boolean showDeathMessages() {
        return getConfig().getBoolean("showDeathMessages", true);
    }

    @Override
    public boolean showAdvancementMessages() {
        return getConfig().getBoolean("showAdvancementMessages", true);
    }

    @Override
    public boolean showAchievementMessages() {
        return getConfig().getBoolean("showAchievementMessages", true);
    }

    @Override
    public boolean forceLink() {
        return getConfig().getBoolean("forceLink", false);
    }

    @Override
    public List<String> getPacks() {
        getConfig().addDefault("packs", new ArrayList<String>());
        return getConfig().getStringList("packs");
    }

    @Override
    public File getRoot() {
        getDataFolder().mkdir();
        return getDataFolder();
    }

    @Override
    public Settings getSettings() {
        return this;
    }

    @Override
    public String getMinecraftVersion() {
        String version = getServer().getVersion();
        version = version.substring(version.lastIndexOf("1."));
        version = version.substring(0, version.lastIndexOf("."));
        return version;
    }

    @Override
    public String chatFormat() {
        return getConfig().getString("format", "<%username%> ");
    }

    @Override
    public String getUserName(UUID uuid) {
        return getUserName(uuid);
    }
}
