package za.net.hanro50.cordiac;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import club.minnced.discord.webhook.external.JDAWebhookClient;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import za.net.hanro50.cordiac.api.Bridge;
import za.net.hanro50.cordiac.api.Client;
import za.net.hanro50.cordiac.api.Server;
import za.net.hanro50.cordiac.api.messages.Achievement;
import za.net.hanro50.cordiac.api.messages.Advancement;
import za.net.hanro50.cordiac.api.messages.BaseMessage;
import za.net.hanro50.cordiac.api.messages.Chat;
import za.net.hanro50.cordiac.api.messages.Death;
import za.net.hanro50.cordiac.api.messages.Join;
import za.net.hanro50.cordiac.api.messages.Leave;
import za.net.hanro50.cordiac.api.messages.Link;
import za.net.hanro50.cordiac.api.messages.Leave.LeaveType;
import za.net.hanro50.cordiac.core.data.Channel;
import za.net.hanro50.cordiac.lang.Parser;
import za.net.hanro50.cordiac.linkers.ChannelLNK;
import za.net.hanro50.cordiac.linkers.PlayerLNK;

public class DiscordServer extends Server {
    final Cache<String, UUID> pendingLink = CacheBuilder.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES)
            .build();
    Parser langParser;
    Bridge bridge;
    Logger log;
    JDA jda;
    final String adminID;
    ChannelLNK channelLinker;
    PlayerLNK playerLNK;
    final Boolean active;
    Map<String, Webhook> cachedWebHooks = new HashMap<>();
    private TrustedList trustedList;

    public DiscordServer(Bridge handler, String token) throws InterruptedException, IOException {
        if (token.equals("Token here")) {
            adminID = null;
            active = false;
            handler.getLogger().info("Please add a discord bot token and reload the plugin");
            return;
        }
        this.trustedList = new TrustedList(handler.getRoot());
        this.active = true;
        this.playerLNK = new PlayerLNK(new File(handler.getRoot(), "Players.json"));
        this.channelLinker = new ChannelLNK(new File(handler.getRoot(), "Channels.json"));
        this.bridge = handler;
        this.log = handler.getLogger();
        jda = JDABuilder.createDefault(token)
                .disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE, CacheFlag.EMOJI, CacheFlag.STICKER,
                        CacheFlag.STICKER, CacheFlag.SCHEDULED_EVENTS)
                .setEnabledIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT,
                        GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_WEBHOOKS, GatewayIntent.GUILD_MESSAGES)
                .setBulkDeleteSplittingEnabled(false)
                // Set activity (like "playing Something")
                .setActivity(Activity.watching("TV")).build();
        jda.addEventListener(new DiscordListener(this));
        jda.awaitReady();
        adminID = jda.retrieveApplicationInfo().complete().getOwner().getId();
        langParser = new Parser(bridge);
    }

    public class messExp extends RuntimeException {
    }

    public class MessageObj {
        final String finalName;
        final String finalProfilePic;
        final String finalLink;
        Channel channel;
        Guild guild;
        Member me;
        TextChannel txt;

        public MessageObj(Client client, UUID id) {
            channel = channelLinker.getChannel(client.getID());
            log.info(client.getID());
            if (channel == null)
                throw new messExp();
            log.info(channel.getID());
            guild = jda.getGuildById(channel.guildID);
            if (guild == null)
                throw new messExp();
            log.info(guild.getName());
            me = guild.getSelfMember();
            if (me == null)
                throw new messExp();
            txt = guild.getTextChannelById(channel.channelID);
            if (txt == null)
                throw new messExp();

            String name = null;
            String pfp = null;
            String link = null;
            String discordID = playerLNK.getDiscordID(id);
            if (discordID != null)
                try {
                    Member mem = guild.retrieveMemberById(discordID).complete();
                    if (mem != null) {
                        log.info("FOUND PFP");
                        name = mem.getEffectiveName();
                        pfp = mem.getEffectiveAvatarUrl();
                        link = "https://discord.com/users/" + mem.getId();
                    } else {
                        log.info("FALLBACK");
                        User user = jda.retrieveUserById(discordID).complete();
                        if (user != null) {
                            name = user.getName();
                            pfp = user.getEffectiveAvatarUrl();
                            link = "https://discord.com/users/" + user.getId();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            if (name == null)
                name = bridge.getUserName(id);
            if (pfp == null)
                pfp = "https://mc-heads.net/avatar/" + id;

            finalName = name;
            finalProfilePic = pfp;
            finalLink = link;
        }

        public MessageObj(Client client, BaseMessage mess) {
            this(client, mess.getPlayerID());
        }

    }

    private void sendMsg(Webhook wb, String name, String PFP, Chat chat) {
        try {
            JDAWebhookClient client = JDAWebhookClient.from(wb);
            WebhookMessageBuilder builder = new WebhookMessageBuilder();
            builder.setUsername(name);
            builder.setAvatarUrl(PFP);
            builder.setContent(chat.getMessage());
            try {
                client.send(builder.build()).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            client.close();
        } catch (RuntimeException e) {
            log.throwing(this.getClass().getName(), "sendMsg", e);
        }
    }

    @Override
    public void sendLinkRequest(Link link) {
        if (pendingLink.getIfPresent(link.getCode()) == null) {
            pendingLink.put(link.getCode(), link.getPlayerID());
        } else {
            /** For safety. We're clearing the cache. */
            pendingLink.invalidateAll();
            log.warning(
                    "Cache collision. Please report if detected as it means our random number generater needs an update!");
        }

    }

    @Override
    public void sendUnLinkRequest(UUID UUID) {
        try {
            playerLNK.removeType(UUID);
        } catch (IOException e) {
            log.warning(
                    "Failed to unlink player!");
            e.printStackTrace();
        }
    }

    public Color getColour(String namespace) {
        if (namespace.startsWith("advancements.adventure"))
            return new Color(254, 109, 0);
        if (namespace.startsWith("advancements.empty"))
            return Color.black;
        if (namespace.startsWith("advancements.end"))
            return new Color(144, 0, 117);
        if (namespace.startsWith("advancements.husbandry"))
            return new Color(164, 78, 43);
        if (namespace.startsWith("advancements.nether"))
            return new Color(254, 59, 0);
        if (namespace.startsWith("advancements.story"))
            return new Color(0, 94, 156);
        if (namespace.startsWith("advancements.sad_label"))
            return Color.pink;
        return Color.white;

    }

    @Override
    public void sendAdvancement(Client client, Advancement advancement) {
        if (!bridge.getSettings().showAdvancementMessages())
            return;
        MessageObj mess;
        try {
            mess = new MessageObj(client, advancement);
        } catch (messExp e) {
            return;
        }
        EmbedBuilder emb = new EmbedBuilder();
        emb.setAuthor(mess.finalName, mess.finalLink,
                mess.finalProfilePic);
        emb.setTitle(langParser.parse(advancement.getNameSpace() + ".title"));
        emb.setDescription(langParser.parse(advancement.getNameSpace() + ".description"));
        emb.setColor(getColour(advancement.getNameSpace()));
        mess.txt.sendMessageEmbeds(emb.build()).submit();
    }

    @Override
    public void sendAchievement(Client client, Achievement achievement) {
        if (!bridge.getSettings().showAchievementMessages())
            return;
        MessageObj mess;
        try {
            mess = new MessageObj(client, achievement);
        } catch (messExp e) {
            return;
        }
        String filler = "???";
        if (achievement.getNameSpace().name.equals("achievement.openInventory")) {
            filler = "e";
        }
        EmbedBuilder emb = new EmbedBuilder();
        emb.setAuthor(mess.finalName, mess.finalLink,
                mess.finalProfilePic);
        emb.setTitle(langParser.parse(achievement.getNameSpace().name));
        emb.setDescription(langParser.parse(achievement.getNameSpace().name + ".desc", filler));
        emb.setColor(achievement.getNameSpace().colour);
        mess.txt.sendMessageEmbeds(emb.build()).submit();
    }

    @Override
    public void sendChat(Client client, Chat chat) {

        MessageObj mess;
        try {
            mess = new MessageObj(client, chat);
        } catch (messExp e) {
            return;
        }
        if (mess.me.hasPermission(mess.txt, Permission.MANAGE_WEBHOOKS)
                && bridge.getSettings().useWebHooks()) {
            Webhook wb = cachedWebHooks.get(mess.channel.getID());
            if (wb == null) {
                mess.txt.retrieveWebhooks().onSuccess(wbs -> {
                    Webhook wba;
                    for (Webhook webhook : wbs) {
                        if (webhook.getOwner() != null && webhook.getOwner().getId().equals(mess.me.getId())) {
                            log.info("OLD STUFF");
                            wba = webhook;
                            cachedWebHooks.put(mess.channel.getID(), webhook);
                            sendMsg(wba, mess.finalName, mess.finalProfilePic, chat);
                            return;
                        }
                    }
                    mess.txt.createWebhook("MC_CHAT_LINK").onSuccess(ff -> {
                        cachedWebHooks.put(mess.channel.getID(), ff);
                        sendMsg(ff, mess.finalName, mess.finalProfilePic, chat);
                    }).submit();
                }).submit();
            } else
                sendMsg(wb, mess.finalName, mess.finalProfilePic, chat);
        } else {
            mess.txt.sendMessage(mess.finalName + ": " + chat.getMessage()).submit();
        }
    }

    @Override
    public void sendDeath(Client client, Death death) {
        if (!bridge.getSettings().showDeathMessages())
            return;
        MessageObj mess;
        try {
            mess = new MessageObj(client, death);
        } catch (messExp e) {
            return;
        }
        String deathMess = death.getNameSpace();
        String cause = death.getMobName(), item = death.getWeapon();
        boolean isPlayer = death.getAttacker() != null;

        EmbedBuilder emb = new EmbedBuilder();

        if (isPlayer) {
            try {
                System.out.print("IS PLAYER!");
                MessageObj attacker = new MessageObj(client, death.getAttacker());
                cause = attacker.finalName;
            } catch (messExp e) {
                e.printStackTrace();
                cause = bridge.getUserName(death.getAttacker());
            }
        }
        boolean hasPlayer = langParser.has(deathMess + ".player");
        log.info(cause == null ? "NULL" : cause.toString());
        if (cause != null) {
            if (!isPlayer && !death.isCustomized()) {
                if (langParser.has(cause.toLowerCase())) {
                    cause = langParser.parse(cause.toLowerCase());
                    if (hasPlayer)
                        deathMess += ".player";
                } else if (langParser.has(cause.toLowerCase() + ".name")) {
                    cause = langParser.parse(cause.toLowerCase() + ".name");
                    if (hasPlayer)
                        deathMess += ".player";
                } else if (!hasPlayer) {
                    log.info(cause);
                    cause = null;
                }
            } else if (hasPlayer) {
                deathMess += ".player";
            }
        }
        if (item != null && cause != null && langParser.has(deathMess + ".item")) {
            deathMess = death.getNameSpace() + ".item";
        }
        emb.setAuthor(langParser.parse(deathMess, mess.finalName, cause, item), mess.finalLink,
                mess.finalProfilePic);

        emb.setColor( new Color(254, 35, 0));
        mess.txt.sendMessageEmbeds(emb.build()).submit();
    }

    @Override
    public void sendJoin(Client client, Join join) {
        if (!bridge.getSettings().showJoinMessages())
            return;
        MessageObj mess;
        try {
            mess = new MessageObj(client, join);
        } catch (messExp e) {
            return;
        }
        EmbedBuilder emb = new EmbedBuilder();
        emb.setAuthor(langParser.parse("multiplayer.player.joined", mess.finalName), mess.finalLink,
                mess.finalProfilePic);
        emb.setColor(Color.green);
        mess.txt.sendMessageEmbeds(emb.build()).submit();
    }

    @Override
    public void sendLeave(Client client, Leave leave) {
        if (!bridge.getSettings().showLeaveMessages())
            return;
        MessageObj mess;
        try {
            mess = new MessageObj(client, leave);
        } catch (messExp e) {
            return;
        }
        EmbedBuilder emb = new EmbedBuilder();
        emb.setAuthor(langParser.parse("multiplayer.player.left", mess.finalName), mess.finalLink,
                mess.finalProfilePic);
        if (leave.getType() != LeaveType.DISCONNECT) {
            emb.setDescription(langParser.parse(leave.getType().code, leave.getReason()));
        }
        emb.setColor(Color.red);
        mess.txt.sendMessageEmbeds(emb.build()).submit();
    }

    @Override
    public void requestData(Client client) {
    }

    @Override
    public void stop() {
        jda.shutdown();
    }

    public void addTrusted(String id) {
        try {
            trustedList.addTrusted(id);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getTrusted() {
        return trustedList.getTrusted();
    }

    @Override
    public boolean active() {
        return active;
    }

}
