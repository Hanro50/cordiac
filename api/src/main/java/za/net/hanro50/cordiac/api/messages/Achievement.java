package za.net.hanro50.cordiac.api.messages;

import java.awt.Color;
import java.util.UUID;

import com.google.gson.annotations.Expose;

public class Achievement extends BaseMessage {
    public static class Struct {
        @Expose
        public String name;
        @Expose
        public Integer colour;

        public Struct() {
        }

        public Struct(String name) {
            this(name, Color.white);
        }

        public Struct(String name, Color colour) {
            this.name = name;
            this.colour = colour.getRGB();
        }
    }

    @Expose
    Struct namespace;

    public Achievement(UUID playerId, Struct namespace) {
        super(playerId);
        this.namespace = namespace;
    }

    public Struct getNameSpace() {
        return namespace;
    }
}
