package za.net.hanro50.cordiac.bridge.spigot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.awt.Color;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import net.md_5.bungee.api.ChatColor;
import za.net.hanro50.interfaces.Client;
import za.net.hanro50.interfaces.Server;
import za.net.hanro50.interfaces.Data.PlayerData;

public class GlobalClient extends Client implements Listener, CommandExecutor {
    YamlConfiguration configYaml = new YamlConfiguration();

    public GlobalClient(Server server, SpigotPlugin spigotPlugin)
            throws FileNotFoundException, IOException, InvalidConfigurationException {
        super(server);
        File ConfFile = new File(spigotPlugin.getDataFolder(), "client.yaml");

        if (ConfFile.exists()) {
            configYaml.load(ConfFile);
        }

        if (configYaml.get("Spigot-Global-Chat") == null)
            configYaml.set("Spigot-Global-Chat", "Global");

        configYaml.save(ConfFile);

        System.out.println(configYaml.get("Spigot-Global-Chat"));
        activate();
    }

    @Override
    public void sendPlayerInformation(PlayerData[] player, boolean refresh) {
    }

    @Override
    public void kick(UUID UUID, String message) {
    }

    @Override
    public void ban(UUID UUID, String message) {
    }

    @Override
    public String getName() {
        return (String) configYaml.get("Spigot-Global-Chat");
    }

    @Override
    public void sendPlayerMessage(Color colourhex, UUID UUID, String message) {
        Bukkit.getPlayer(UUID).chat(message);

    }

    @Override
    public void sendServerMessage(Color colourhex, String DiscordUsername, String message) {
        Bukkit.getServer()
                .broadcastMessage(ChatColor.of(colourhex) + "<" + DiscordUsername + "> " + ChatColor.WHITE + message);

    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent evt) {
        final Player p = evt.getPlayer();
        final String msg = evt.getMessage();
        this.server.sendMessage(this, p.getUniqueId(), msg);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent evt) {
        this.server.playerJoin(this, evt.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerLeave(PlayerQuitEvent evt) {
        this.server.playerLeave(this, evt.getPlayer().getUniqueId());
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            switch (command.getName()) {
                case ("link"):
                    String code = String.format("%06d", ThreadLocalRandom.current().nextInt(100000, 1000000));
                    player.sendMessage("DM the bot the following message [" + ChatColor.RED + "!link " + code
                            + ChatColor.RESET + "] to link your account");
                    server.sendLinkRequest(player.getUniqueId(), code);
                    break;
                case ("unlink"):
                    server.sendUnLinkRequest(player.getUniqueId());

            }
        }

        // If the player (or console) uses our command correct, we can return true
        return true;

    }

}
