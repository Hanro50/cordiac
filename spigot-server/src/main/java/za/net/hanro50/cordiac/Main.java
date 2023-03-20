package za.net.hanro50.cordiac;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import za.net.hanro50.cordiac.api.Bridge;
import za.net.hanro50.cordiac.api.Settings;
import za.net.hanro50.cordiac.api.messages.Link;

public class Main extends JavaPlugin implements Settings, Bridge {
    DiscordServer server;

    @Override
    public void onEnable() {
        try {
            saveDefaultConfig();
            try {
                server = new DiscordServer(this, getConfig().getString("token", "Token here"));
                if (!server.active())
                    return;

            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
                return;
            }
            try {
                Class.forName("org.bukkit.event.player.PlayerAdvancementDoneEvent");
                Plugin pl = new Plugin(this, this, server, "Global");
                this.getServer().getPluginManager().registerEvents(pl, this);
            } catch (ClassNotFoundException e) {
                getLogger().info("Failed to load current implementation. Loading fallback!");
                OldPlugin pl = new OldPlugin(this, this, server, "Global");
                this.getServer().getPluginManager().registerEvents(pl, this);
            }
            this.getCommand("link").setExecutor(this);
            this.getCommand("unlink").setExecutor(this);
        } catch (java.lang.NoSuchMethodError e) {
            System.out.println("WARNING: Outdated server. Cannot continue!");
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
        return getConfig().getString("format", "<%username%> %message%");
    }

    @Override
    public String getUserName(UUID uuid) {
        OfflinePlayer plr = Bukkit.getOfflinePlayer(uuid);
        if (plr.isOnline())
            return Bukkit.getPlayer(uuid).getDisplayName();
        return plr.getName();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, @NotNull String label,
            @NotNull String[] args) {
        if (sender instanceof Player) {

            Player player = (Player) sender;
            switch (command.getName()) {
                case ("link"):
                    final Link link = new Link(player.getUniqueId());
                    final String format = getConfig().getString("linkText",
                            "Please DM the bot the following message \"!link %s\"");
                    server.sendLinkRequest(link);// linkText
                    player.sendMessage(String.format(format, link.getCode()));
                    break;
                case ("unlink"):
                    server.sendUnLinkRequest(player.getUniqueId());
                    player.sendMessage(getConfig().getString("unLinkText",
                            "Request has been sent to unlink your profile!"));
            }
        }

        // If the player (or console) uses our command correct, we can return true
        return true;

    }
}
