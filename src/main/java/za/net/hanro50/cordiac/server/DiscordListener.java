package za.net.hanro50.cordiac.server;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import za.net.hanro50.interfaces.Client;
import za.net.hanro50.interfaces.Data.Channel;

public class DiscordListener extends ListenerAdapter {
    final Discord discord;
    final Map<String, BiConsumer<String[], MessageReceivedEvent>> eventHandler = new HashMap<>();

    public DiscordListener(Discord discord) {
        this.discord = discord;
        eventHandler.put("!set", (split, event) -> {
            Channel chan = new Channel(event.getGuild().getId(), event.getGuildChannel().getId());
            if (split.length < 2) {
                String res = "Usage !set <room ID>\nAvailable room IDs\n";
                for (Entry<String, Client> client : this.discord.getClients().entrySet()) {
                    res += client.getKey();
                }
                event.getGuildChannel().sendMessage(res).submit();
                return;
            } else if (discord.getClients().get(split[1]) != null) {
                discord.handler.linkServer(chan, split[1]);
                event.getGuildChannel().sendMessage("Channel has been linked to " + split[1]).submit();
                return;
            } else {
                event.getGuildChannel().sendMessage("Invalid room ID provided").submit();
                return;
            }
        });
        eventHandler.put("!trust", (split, event) -> {
            User usr = event.getMessage().getMentions().getUsers().get(0);
            if (usr != null) {
                discord.handler.addTrusted(usr.getIdLong());
                event.getGuildChannel().sendMessage(usr.getAsMention() + " is now trusted!").submit();
                return;
            }
            event.getGuildChannel().sendMessage("Usage: !trust @User").submit();
            return;
        });
    }

    Number getColor(MessageReceivedEvent event) {
        Member mem = event.getMember();
        if (mem != null) {
            return mem.getColor().getRGB();
        } else {
            return 16777214;
        }
    }

    String getName(MessageReceivedEvent event) {
        Member mem = event.getMember();
        if (mem != null) {
            return mem.getEffectiveName();
        } else {
            return event.getAuthor().getName();
        }
    }

    public boolean isTrusted(MessageReceivedEvent event) {
        discord.log.info(discord.handler.getTrusted()+"_"+event.getAuthor().getIdLong());
        return (discord.handler.getTrusted().contains(event.getAuthor().getIdLong())
                || discord.adminID == event.getAuthor().getIdLong());
    }

    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        String message = event.getMessage().getContentStripped();
        if (event.isFromGuild() && !event.isWebhookMessage()
                && !event.getAuthor().getId().equals(event.getJDA().getSelfUser().getId())) {
            IMentionable mem = event.getMember();
            Color color = null;
            if (mem == null)
                mem = event.getAuthor();
            else
                color = event.getMember().getColor();
            if (color == null)
                color = Color.white;
            Channel chan = new Channel(event.getGuild().getId(), event.getGuildChannel().getId());
            String[] com = message.split(" ");
            if (eventHandler.containsKey(com[0])) {
                if (!isTrusted(event))
                    event.getGuildChannel().sendMessage("Insufficient permissions, the bot does not trust you")
                            .submit();
                else
                    eventHandler.get(com[0]).accept(com, event);
                return;

            }
            Client client = discord.getClients().get(discord.handler.getChannelName(chan));

            if (client != null) {
                client.sendServerMessage(color, getName(event), message);
            }

        }
    }

}
