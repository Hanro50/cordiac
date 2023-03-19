package za.net.hanro50.cordiac;

import java.io.IOException;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.projectiles.ProjectileSource;

import za.net.hanro50.cordiac.api.Bridge;
import za.net.hanro50.cordiac.api.Client;
import za.net.hanro50.cordiac.api.DPlayer;
import za.net.hanro50.cordiac.api.Server;
import za.net.hanro50.cordiac.api.messages.Achievement;
import za.net.hanro50.cordiac.api.messages.Chat;
import za.net.hanro50.cordiac.api.messages.Death;
import za.net.hanro50.cordiac.api.messages.Join;
import za.net.hanro50.cordiac.api.messages.Leave;
import za.net.hanro50.cordiac.api.messages.Leave.LeaveType;

//OLD API BINDING.
//UNSTABLE AND UNSUPPORTED!
@SuppressWarnings("deprecation")
public class OldPlugin implements Listener, Client {
    final String name;
    final Server server;
    final JavaPlugin plugin;
    final Bridge bridge;

    public OldPlugin(JavaPlugin plugin, Bridge bridge, Server server, String name) {
        this.plugin = plugin;
        this.bridge = bridge;
        this.server = server;
        this.name = name;
        activate(server);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    void onPlayerChat(PlayerChatEvent e) {
        server.sendChat(this, new Chat(e.getPlayer().getUniqueId(), e.getMessage()));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    void onPlayerLoginEvent(PlayerLoginEvent e) {
        server.sendJoin(this, new Join(e.getPlayer().getUniqueId()));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    void onPlayerLeaveEvent(PlayerQuitEvent e) {
        if (e.getPlayer().isBanned()) {
            server.sendLeave(this, new Leave(e.getPlayer().getUniqueId(), LeaveType.BAN, ""));
        } else
            server.sendLeave(this, new Leave(e.getPlayer().getUniqueId(), LeaveType.DISCONNECT, null));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    void onPlayerKickEvent(PlayerKickEvent e) {
        server.sendLeave(this, new Leave(e.getPlayer().getUniqueId(), LeaveType.KICK, e.getReason()));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onAchievement(PlayerAchievementAwardedEvent achievement) {
        server.sendAchievement(this, new Achievement(achievement.getPlayer().getUniqueId(),
                ReMappings.AchievementTranslate(achievement.getAchievement())));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDeath(PlayerDeathEvent death) {
        Death A = null;
        UUID player = null;
        boolean isCustomized = false;
        if (death.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent LastD = (EntityDamageByEntityEvent) death.getEntity().getLastDamageCause();
            String Weapon = null;
            Entity Damager = LastD.getDamager();
            String DeathMessage = ReMappings.DMTranslate(LastD.getCause());
            String Mob = null;

            // TNT is annoying
            if (Damager instanceof TNTPrimed) {
                Damager = ((TNTPrimed) Damager).getSource();
            } else if (Damager instanceof Projectile) {
                ProjectileSource shooter = ((Projectile) Damager).getShooter();
                if (shooter instanceof Entity) {
                    Damager = (Entity) shooter;
                } else {
                    plugin.getLogger().info(shooter.toString());
                }
            } else if (Damager instanceof FallingBlock) {
                // death.attack.anvil
                FallingBlock block = (FallingBlock) Damager;

                Material f = block.getMaterial();
                if (f == Material.ANVIL) {
                    DeathMessage = "death.attack.anvil";
                }
                Damager = null;
            }

            // TNT can sometimes do weird stuff
            if (Damager != null) {

                String mobT = "entity.minecraft." + Damager.getType().getName();
                Mob = mobT;
                if (Damager.getCustomName() != null) {
                    Mob = Damager.getCustomName();
                    isCustomized = true;
                }
                if (Damager instanceof LivingEntity) {
                    ItemMeta Meta = null;
                    EntityEquipment inv = ((LivingEntity) Damager).getEquipment();

                    ItemMeta MetaT = inv.getItemInHand().getItemMeta();
                    Meta = MetaT;

                    // }
                    try {
                        if (Meta.hasDisplayName()) {
                            Weapon = Meta.getDisplayName();
                        }
                    } catch (NullPointerException err) {
                        plugin.getLogger().info("Nullpointer? Assuming item has no meta data then");
                    }
                }

                if (Damager instanceof Player) {
                    player = Damager.getUniqueId();
                }
            }

            A = new Death(DeathMessage, Mob, death.getEntity().getUniqueId(), player, Weapon,isCustomized);

        } else {
            if (death.getEntity().getKiller() != null) {
                player = death.getEntity().getKiller().getUniqueId();
            }
            A = new Death(ReMappings.DMTranslate(death.getEntity().getLastDamageCause().getCause()), null,
                    death.getEntity().getUniqueId(), player, null,isCustomized);
        }

        if (A != null) {
            server.sendDeath(this, A);
            // Output
        }
    }

    @Override
    public void message(String message) {
        plugin.getServer().broadcastMessage(message);
    }

    @Override
    public void message(DPlayer player, String message) {
        String env = bridge.chatFormat();
        env = env.replace("%nickname%", player.getNickName());
        env = env.replace("%username%", player.getUserName());
        env = env.replace("%color%", org.bukkit.ChatColor.AQUA + "");
        if (player.getRoles().isEmpty())
            env = env.replace("%role%", "");
        else
            env = env.replace("%role%", player.getRoles().get(0).name);
        env = env.replace("%message%", message);

        this.message(env);

    }

    @Override
    public void kick(DPlayer player, String reason) {
        Entity ent = plugin.getServer().getEntity(player.getUuid());
        if (ent instanceof Player) {
            ((Player) ent).kickPlayer(reason);
        }
    }

    @Override
    public void ban(DPlayer player, String reason) {

    }

    @Override
    public void info(DPlayer player) {
        try {
            new DPlayer(bridge, player);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getID() {
        return this.name;
    }
}
