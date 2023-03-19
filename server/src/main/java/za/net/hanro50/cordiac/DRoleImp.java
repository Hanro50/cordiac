package za.net.hanro50.cordiac;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import za.net.hanro50.cordiac.api.DRole;

public class DRoleImp extends DRole{
    public DRoleImp() {
    }

    public DRoleImp(Role role) {
        this.id = role.getIdLong();
        this.color = role.getColor();
        this.name = role.getName();
        this.admin = role.hasPermission(Permission.ADMINISTRATOR);
        this.guildID = role.getGuild().getIdLong();
    }
}
