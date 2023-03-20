package za.net.hanro50.cordiac;

import org.spongepowered.api.Server;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.StartedEngineEvent;
import org.spongepowered.api.event.lifecycle.StoppingEngineEvent;
import org.spongepowered.api.event.message.PlayerChatEvent;
import org.spongepowered.plugin.builtin.jvm.Plugin;
/**
 * Hello world!
 *
 */
// Imports for logger
import com.google.inject.Inject;
import org.apache.logging.log4j.Logger;

@Plugin("Cordiac")
public class Cordiac {

    @Inject
    private Logger logger;

    @Listener
    public void onServerStart(final StartedEngineEvent<Server> event) {
        logger.info("Successfully running ExamplePlugin!!!");
    }

    @Listener
    public void onServerStop(final StoppingEngineEvent<Server> event) {
        logger.info("Successfully running ExamplePlugin!!!");
    }
    @Listener
    public void onPlayerChat(final PlayerChatEvent chat) {
        logger.info("CHAT TEST!");
        logger.info(chat.message());
    }

}