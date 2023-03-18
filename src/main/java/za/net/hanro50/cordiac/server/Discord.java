package za.net.hanro50.cordiac.server;

import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;

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
import za.net.hanro50.interfaces.Client;
import za.net.hanro50.interfaces.Handler;
import za.net.hanro50.interfaces.Server;
import za.net.hanro50.interfaces.Data.Cause;
import za.net.hanro50.interfaces.Data.Channel;

public class Discord extends Server {
    final Handler handler;
    final JDA jda;
    final Logger log;
    final Cache<String, UUID> pendingLink = CacheBuilder.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES).build();
    long adminID;

    Map<String, Webhook> cachedWebHooks = new HashMap<>();

    public Discord(Handler handler, Logger logger2) throws InterruptedException {
        this.handler = handler;
        this.log = logger2;
        jda = JDABuilder.createDefault(handler.getDiscordToken())
                .disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE, CacheFlag.EMOJI, CacheFlag.STICKER,
                        CacheFlag.STICKER, CacheFlag.SCHEDULED_EVENTS)
                .setEnabledIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT,
                        GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_WEBHOOKS, GatewayIntent.GUILD_MESSAGES)
                .setBulkDeleteSplittingEnabled(false)
                // Set activity (like "playing Something")
                .setActivity(Activity.watching("TV")).build();
        jda.addEventListener(new DiscordListener(this));
        jda.awaitReady();
        adminID = jda.retrieveApplicationInfo().complete().getOwner().getIdLong();
    }

    private void sendMsg(Webhook wb, String name, String PFP, String message) {
        try {
            @NotNull
            JDAWebhookClient client = JDAWebhookClient.from(wb);
            WebhookMessageBuilder builder = new WebhookMessageBuilder();
            builder.setUsername(name);
            builder.setAvatarUrl(PFP);
            builder.setContent(message);
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

    public class messExp extends RuntimeException {
    }

    public class messageObj {
        final String finalName;
        final String finalProfilePic;
        final String finalLink;
        Channel channel;
        Guild guild;
        Member me;
        TextChannel txt;

        public messageObj(Client client, UUID UUID) {
            channel = handler.getChannelLinker().getChannel(client.getName());
            log.info(client.getName());
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
            String discordID = handler.getPlayerLinker().getDiscordID(UUID);
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
                name = handler.getMCUsername(UUID);
            if (pfp == null)
                pfp = "https://mc-heads.net/avatar/" + UUID;

            finalName = name;
            finalProfilePic = pfp;
            finalLink = link;
        }
    }

    @Override
    public void sendMessage(Client client, UUID UUID, final String message) {
        messageObj mess;
        try {
            mess = new messageObj(client, UUID);
        } catch (messExp e) {
            return;
        }
        if (mess.me.hasPermission(mess.txt, Permission.MANAGE_WEBHOOKS)
                && (handler.Settings().get("UseWebHooks") == true)) {
            Webhook wb = cachedWebHooks.get(mess.channel.getID());
            if (wb == null) {
                mess.txt.retrieveWebhooks().onSuccess(wbs -> {
                    Webhook wba;
                    for (Webhook webhook : wbs) {
                        if (webhook.getOwner() != null && webhook.getOwner().getId().equals(mess.me.getId())) {
                            log.info("OLD STUFF");
                            wba = webhook;
                            cachedWebHooks.put(mess.channel.getID(), webhook);
                            sendMsg(wba, mess.finalName, mess.finalProfilePic, message);
                            return;
                        }
                    }
                    mess.txt.createWebhook("MC_CHAT_LINK").onSuccess(ff -> {
                        cachedWebHooks.put(mess.channel.getID(), ff);
                        sendMsg(ff, mess.finalName, mess.finalProfilePic, message);
                    }).submit();
                }).submit();
            } else
                sendMsg(wb, mess.finalName, mess.finalProfilePic, message);
        } else {
            mess.txt.sendMessage(mess.finalName + ": " + message).submit();
        }
    }

    @Override
    public void playerJoin(Client client, UUID UUID) {
        if (handler.Settings().get("JoinMessages") == false)
            return;
        messageObj mess;
        try {
            mess = new messageObj(client, UUID);
        } catch (messExp e) {
            return;
        }
        EmbedBuilder emb = new EmbedBuilder();
        emb.setAuthor(handler.getLangParser().parse("multiplayer.player.joined", mess.finalName), mess.finalLink,
                mess.finalProfilePic);
        emb.setColor(Color.green);
        mess.txt.sendMessageEmbeds(emb.build()).submit();

    }

    @Override
    public void playerLeave(Client client, UUID UUID) {
        if (handler.Settings().get("LeaveMessages") == false)
            return;
        messageObj mess;
        try {
            mess = new messageObj(client, UUID);
        } catch (messExp e) {
            return;
        }
        EmbedBuilder emb = new EmbedBuilder();
        emb.setAuthor(handler.getLangParser().parse("multiplayer.player.left", mess.finalName), mess.finalLink, mess.finalProfilePic);
        emb.setColor(Color.red);
        mess.txt.sendMessageEmbeds(emb.build()).submit();

    }

    @Override
    public void sendAdvancement(Client client, String AdvancementId, UUID playerUUID) {

    }

    @Override
    public void sendDeathMessage(Client client, String DeathId, UUID playerUUID, Cause cause) {

    }

    @Override
    public void stop() {
        jda.shutdown();
    }

    @Override
    public void requestData(Client client) {
        throw new UnsupportedOperationException("Unimplemented method 'requestData'");
    }

    @Override
    public void sendUnLinkRequest(UUID UUID) {
        try {
            handler.getPlayerLinker().removeType(UUID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendLinkRequest(UUID UUID, String code) {
        pendingLink.put(code, UUID);
    }
}
