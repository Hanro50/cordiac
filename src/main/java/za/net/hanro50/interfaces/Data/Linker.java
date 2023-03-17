package za.net.hanro50.interfaces.Data;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.HashBiMap;

public class Linker<Key, Type> {
    public final String _COMMENT = "THIS IS AN INTERNAL DATA FILE, DO NOT EDIT!";

    public Map<Key, Type> data = new HashMap<>();

    public Linker() {

        System.out.println("Linker file loaded!");
    }

    protected Map<Type, Key> getInverse() {
        if (!(data instanceof HashBiMap))
            data = HashBiMap.create(data);
        return ((HashBiMap<Key, Type>) data).inverse();
    }
}
