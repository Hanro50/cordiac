package za.net.hanro50.cordiac.server;

import java.util.Date;
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
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
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

    @Override
    public void sendMessage(Client client, UUID UUID, String message) {
        String name = handler.getMCUsername(UUID);
        String pfp = "https://mc-heads.net/avatar/" + UUID;
        Channel channel = handler.getChannel(client.getName());
        log.info(client.getName());
        if (channel == null)
            return;
        log.info(channel.getID());
        Guild guild = jda.getGuildById(channel.guildID);
        if (guild == null)
            return;
        log.info(guild.getName());
        Member me = guild.getSelfMember();
        if (me == null)
            return;

        TextChannel txt = guild.getTextChannelById(channel.channelID);
        if (txt == null)
            return;
        if (me.hasPermission(txt, Permission.MANAGE_WEBHOOKS)) {
            Webhook wb = cachedWebHooks.get(channel.getID());
            if (wb == null) {
                txt.retrieveWebhooks().onSuccess(wbs -> {
                    Webhook wba;
                    for (Webhook webhook : wbs) {
                        if (webhook.getOwner() != null && webhook.getOwner().getId().equals(me.getId())) {
                            log.info("OLD STUFF");
                            wba = webhook;
                            cachedWebHooks.put(channel.getID(), webhook);
                            sendMsg(wba, name, pfp, message);
                            return;
                        }
                    }
                    txt.createWebhook("MC_CHAT_LINK").onSuccess(ff -> {
                        cachedWebHooks.put(channel.getID(), ff);
                        sendMsg(ff, name, pfp, message);
                    }).submit();
                }).submit();
            } else
                sendMsg(wb, name, pfp, message);
        } else {
            txt.sendMessage(name + ": " + message).submit();
        }
    }

    @Override
    public void playerJoin(Client client, UUID UUID) {

    }

    @Override
    public void playerLeave(Client client, UUID UUID) {

    }

    @Override
    public void sendAdvancement(Client client, String AdvancementId, UUID playerUUID) {

    }

    @Override
    public void sendDeathMessage(Client client, String DeathId, UUID playerUUID, Cause cause) {

    }

    @Override
    public void getPlayerInformation(Client client, UUID[] UUIDs) {
    }

    @Override
    public void stop() {
        jda.shutdown();
    }

    @Override
    public void requestData(Client client) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'requestData'");
    }

    @Override
    public void sendUnLinkRequest(UUID UUID) {
    }

    @Override
    public void sendLinkRequest(UUID UUID, String code) {
        pendingLink.put(code, UUID);
    }
}
