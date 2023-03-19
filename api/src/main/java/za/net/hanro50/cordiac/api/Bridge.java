package za.net.hanro50.cordiac.api;

import java.io.File;
import java.util.UUID;
import java.util.logging.Logger;

public interface Bridge {
    public Logger getLogger();

    public File getRoot();

    public Settings getSettings();

    public String getUserName(UUID uuid);

    public String getMinecraftVersion();

    public String chatFormat();
}
