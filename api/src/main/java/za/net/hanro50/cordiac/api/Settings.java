package za.net.hanro50.cordiac.api;

import java.util.List;

public interface Settings {
    public boolean useWebHooks();

    public boolean showJoinMessages();

    public boolean showLeaveMessages();

    public boolean showDeathMessages();

    public boolean showAdvancementMessages();

    public boolean showAchievementMessages();

    public boolean forceLink();

    public List<String> getPacks();
}
