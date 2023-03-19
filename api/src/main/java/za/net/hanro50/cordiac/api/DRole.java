package za.net.hanro50.cordiac.api;

import java.awt.Color;

import com.google.gson.annotations.Expose;

public class DRole {
    @Expose
    public Long id;
    @Expose
    public Color color;
    @Expose
    public String name;
    @Expose
    public boolean admin;
    @Expose
    public Long guildID;

    public DRole() {
    }
}
