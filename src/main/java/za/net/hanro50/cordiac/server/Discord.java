package za.net.hanro50.cordiac.server;

import java.util.logging.Handler;

import za.net.hanro50.interfaces.Client;
import za.net.hanro50.interfaces.Server;
import za.net.hanro50.interfaces.Data.Cause;

public class Discord extends Server {
    final Handler handler;

    public Discord(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void sendLinkRequest(String UUID, String code) {

    }

    @Override
    public void sendMessage(Client client, String message) {

    }

    @Override
    public void playerJoin(Client client, String UUID) {

    }

    @Override
    public void playerLeave(Client client, String UUID) {

    }

    @Override
    public void sendAdvancement(Client client, String AdvancementId, String playerUUID) {

    }

    @Override
    public void requestData(Client client) {

    }

    @Override
    public void sendDeathMessage(Client client, String DeathId, String playerUUID, Cause cause) {

    }

    @Override
    public void getPlayerInformation(Client client, String[] UUIDs) {
    }

}
