package za.net.hanro50.cordiac;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.function.BiConsumer;

import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import za.net.hanro50.cordiac.api.Client;
import za.net.hanro50.cordiac.core.data.Channel;
import za.net.hanro50.cordiac.lang.LangInfo;

public class DiscordListener extends ListenerAdapter {
    final DiscordServer discord;
    final Map<String, BiConsumer<String[], MessageReceivedEvent>> eventHandler = new HashMap<>();

    public DiscordListener(DiscordServer discord) {
        this.discord = discord;
        eventHandler.put("!set", (split, event) -> {
            Channel chan = new Channel(event.getGuild().getId(), event.getGuildChannel().getId());
            if (split.length < 2) {
                String res = "Usage !set <room ID>\nAvailable room IDs\n";
                for (Entry<String, Client> client : this.discord.getClients().entrySet()) {
                    res += client.getKey() + "\n";
                }
                event.getGuildChannel().sendMessage(res).submit();
                return;
            } else if (discord.getClients().get(split[1]) != null) {
                try {
                    discord.channelLinker.link(chan, split[1]);

                    event.getGuildChannel().sendMessage("Channel has been linked to " + split[1]).submit();
                } catch (IOException e) {
                    event.getGuildChannel().sendMessage("Failed to link " + split[1] + "\n" + e.getLocalizedMessage())
                            .submit();
                    e.printStackTrace();
                }
                return;
            } else {
                event.getGuildChannel().sendMessage("Invalid room ID provided").submit();
                return;
            }
        });
        eventHandler.put("!trust", (split, event) -> {
            User usr = event.getMessage().getMentions().getUsers().get(0);
            if (usr != null) {
                discord.addTrusted(usr.getId());
                event.getGuildChannel().sendMessage(usr.getAsMention() + " is now trusted!").submit();
                return;
            }
            event.getGuildChannel().sendMessage("Usage: !trust @User").submit();
            return;
        });
        eventHandler.put("!language", (split, event) -> {
            LangInfo info = this.discord.langParser.getInfo();
            if (split.length < 2) {
                String res = "Usage !language <language ID>\nAvailable language IDs\n\nID : Name\n";
                List<Entry<String, String>> z = new ArrayList<>(info.info.entrySet());
                z.sort((a, b) -> {
                    return a.getKey().compareTo(b.getKey());
                });
                for (Entry<String, String> client : z) {
                    String t = "";
                    if (client.getKey().equals(info.current))
                        t += ">>";

                    t += client.getKey() + " : " + client.getValue() + "\n";
                    if (res.length() + t.length() > 1950) {
                        event.getGuildChannel().sendMessage(res).submit();
                        res = t;
                    } else {
                        res += t;
                    }

                }
                event.getGuildChannel().sendMessage(res).submit();
                return;
            } else if (info.info.keySet().contains(split[1])) {
                try {
                    discord.langParser.setLang(split[1], true);
                    event.getGuildChannel()
                            .sendMessage("Language has been set to " + info.info.getOrDefault(split[1], split[1]))
                            .submit();
                } catch (IOException e) {
                    event.getGuildChannel()
                            .sendMessage("Failed to link " + split[1] + "\n" + e.getLocalizedMessage())
                            .submit();
                    e.printStackTrace();
                }
            } else {
                event.getGuildChannel().sendMessage(
                        "Failed to find language code provided. Perhaps it has not been set to download the particular language pack you wanted?")
                        .submit();
            }
            return;

        });

    }

    public boolean isTrusted(MessageReceivedEvent event) {
        discord.log.info(discord.getTrusted() + "_" + event.getAuthor().getIdLong());
        return (discord.getTrusted().contains(event.getAuthor().getId())
                || discord.adminID.equals(event.getAuthor().getId()));
    }

    public void onMessageReceived(MessageReceivedEvent event) {
        String message = event.getMessage().getContentStripped();
        discord.log.info(message);
        String[] com = message.split(" ");
        if (event.isWebhookMessage()
                || event.getAuthor().getId().equals(event.getJDA().getSelfUser().getId()))
            return;
        if (event.isFromGuild()) {
            IMentionable mem = event.getMember();
            Color color = null;
            if (mem == null)
                mem = event.getAuthor();
            else
                color = event.getMember().getColor();
            if (color == null)
                color = Color.white;
            Channel chan = new Channel(event.getGuild().getId(), event.getGuildChannel().getId());

            if (eventHandler.containsKey(com[0])) {
                if (!isTrusted(event))
                    event.getGuildChannel().sendMessage("Insufficient permissions, the bot does not trust you")
                            .submit();
                else
                    eventHandler.get(com[0]).accept(com, event);
                return;

            }
            Client client = discord.getClients().get(discord.channelLinker.getChannelName(chan));
            try {
                if (client != null) {
                    if (event.getMember() != null)
                        client.message(new DPlayerImp(discord, event.getMember()), message);
                    else if (event.getMember() != null)
                        client.message(new DPlayerImp(discord, event.getAuthor()), message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        if (message.startsWith("!link")) {
            if (com.length != 2) {
                event.getChannel().sendMessage("Usage !trust <code>\nRun the /link command in MC to get a code.")
                        .submit();
            } else {
                UUID uuid = discord.pendingLink.getIfPresent(com[1]);
                if (uuid != null)
                    try {
                        discord.playerLNK.link(event.getAuthor().getId(), uuid);
                        // Simple test
                        discord.log.info("[LINKED] " + discord.playerLNK.getDiscordID(uuid)
                                + " <=======> " + discord.playerLNK.getUUID(event.getAuthor().getId()));
                        event.getChannel().sendMessage("Linked accounts!").submit();
                        discord.pendingLink.invalidate(com[1]);
                    } catch (IOException e) {
                        e.printStackTrace();
                        event.getChannel().sendMessage("Error, failed to link\n" + e.getMessage()).submit();
                    }
                else
                    event.getChannel().sendMessage("Error, failed to link\nCould not find provided code!").submit();
            }
        }

    }

}
