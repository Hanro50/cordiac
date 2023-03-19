package za.net.hanro50.cordiac;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;

import za.net.hanro50.cordiac.core.Util;

public class TrustedList {
    @Expose
    Set<String> trusted = new HashSet<>();
    private static Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
    private File base;

    public TrustedList(File root) throws JsonSyntaxException, IOException {
        base = new File(root, "trusted.json");
        if (base.exists())
            trusted = gson.fromJson(Util.readFile(base), TrustedList.class).trusted;
    }

    void addTrusted(String id) throws IOException {
        trusted.add(id);
        Util.write(base, gson.toJson(this));
    }

    public List<String> getTrusted() {
        return new ArrayList<>(trusted);
    }
}
