package za.net.hanro50.cordiac.server;

import za.net.hanro50.interfaces.Client;
import za.net.hanro50.interfaces.Server;
import za.net.hanro50.interfaces.Data.Cause;

public class Discord extends Server{

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
    
}
