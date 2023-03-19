package za.net.hanro50.cordiac.linkers;

import java.io.File;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.HashBiMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import za.net.hanro50.cordiac.core.Util;

public class Linker<Key,Type> {
    @Expose
    public final String _COMMENT = "THIS IS AN INTERNAL DATA FILE, DO NOT EDIT!";
    @Expose
    public Map<Key, Type> data = new HashMap<>();

    private File mainFile;
    private static Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();

    public <T extends Linker<Key, Type>> Linker(File file, Class<T> ClassOfT) throws IOException {
        mainFile = file;
        if (mainFile.exists()) {
            Linker<Key, Type> LNK = gson.fromJson(Util.readFile(mainFile), ClassOfT);
            this.data = LNK.data;
        }
    }

    protected Linker() {}

    public void link(Key k, Type t) throws IOException {
        data.put(k, t);
        save();
    }

    public void removeKey(Key k) throws IOException {
        data.remove(k);
        save();
    }
    public void removeType(Type k) throws IOException {
        getInverse().remove(k);
        save();
    }

    public void save() throws IOException {
        Util.write(mainFile, gson.toJson(this));
    }

    protected Map<Type, Key> getInverse() {
        if (!(data instanceof HashBiMap))
            data = HashBiMap.create(data);
        return ((HashBiMap<Key, Type>) data).inverse();
    }
}
