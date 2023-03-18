package za.net.hanro50.cordiac.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

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
import za.net.hanro50.cordiac.bridge.SpigotPlugin;
import za.net.hanro50.interfaces.Client;
import za.net.hanro50.interfaces.Server;
import za.net.hanro50.interfaces.Data.PlayerData;

public class SpigotClient extends Client implements Listener, CommandExecutor {
    YamlConfiguration configYaml = new YamlConfiguration();
    String format = "<%username%> : %message%";

    public SpigotClient(Server server, SpigotPlugin spigotPlugin, String name)
            throws FileNotFoundException, IOException, InvalidConfigurationException {
        super(server);
        File ConfFile = new File(spigotPlugin.getDataFolder(), "client.yaml");

        if (ConfFile.exists()) {
            configYaml.load(ConfFile);
        }

        if (configYaml.get(name + ".name") == null) {
            List<String> lst = new ArrayList<>();
            lst.add("The name the channel fed to the /list command on discord. Should be unique in a bungeecord setup");
            configYaml.setComments(name + ".name", lst);
            configYaml.set(name + ".name", name);
        }
        if (configYaml.get(name + ".format") == null) {
            List<String> lst = new ArrayList<>();
            lst.add("Used for chat formatting!");
            lst.add("Available place holdersformat:");
            lst.add("\t%nickName%: Discord Username/Nickname");
            lst.add("\t%message%: The user's username");
            lst.add("\t%username%: MC Username");
            lst.add("\t%role%: A user's top role");
            lst.add("\t%color%: A user's top role color");
            lst.add("\t%linked%: Can be either 'Linked' or 'Unlinked'");
            configYaml.setComments(name + ".format", lst);
            configYaml.set(name + ".format", "[%color%%role%%reset%] <%username%> : %message%");
        }
        format = (String) configYaml.get(name + ".format");
        // # Available place holdersformat:

        configYaml.save(ConfFile);

        System.out.println(configYaml.get("Spigot-Global-Chat"));
        activate();
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
    public void sendMessage(PlayerData player, String message) {
        Bukkit.getServer()
                .broadcastMessage(
                        ChatColor.of(player.color) + "<" + player.userName + "> " + ChatColor.WHITE + message);

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
                    String code = String.format("%03.3d-%03d-%03d", Math.floor(new Date().getTime() / 100.0),
                            ThreadLocalRandom.current().nextInt(0, 1000), ThreadLocalRandom.current().nextInt(0, 1000));
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

    @Override
    public void sendPlayerInformation(PlayerData player) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendPlayerInformation'");
    }

}
