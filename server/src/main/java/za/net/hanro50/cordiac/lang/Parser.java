package za.net.hanro50.cordiac.lang;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import za.net.hanro50.cordiac.api.Bridge;
import za.net.hanro50.cordiac.core.Util;

public class Parser {
    final static String manifest = "https://piston-meta.mojang.com/mc/game/version_manifest.json";
    final static Gson gson = new GsonBuilder().setLenient().create();
    final static Type mapToken = new TypeToken<Map<String, String>>() {
    }.getType();
    final Logger logger;
    Config config;
    Map<String, String> mappings = new HashMap<String, String>();
    File conf;
    File cache;
    File base;
    LangInfo info;

    public String parse(String code, Object... args) {
        return String.format(mappings.getOrDefault(code, code), args);
    }

    public LangInfo getInfo() {
        if (info == null)
            info = new LangInfo(config.selected, cache, gson);
        return info;
    }

    public void setLang(String code, boolean save) throws IOException {

        File finalFile = new File(cache, code + ".json");
        if (finalFile.exists()) {
            logger.info("Loading language: " + code);
            config.selected = code;
            if (save)
                Util.write(conf, gson.toJson(config));

            mappings = gson.fromJson(Util.readFile(finalFile), mapToken);
        } else {
            new FileNotFoundException();
        }
    }

    public Parser(Bridge bridge)
            throws MalformedURLException, IOException {
        List<String> packs = bridge.getSettings().getPacks();
        this.logger = bridge.getLogger();
        this.base = new File(bridge.getRoot(), "lang");
        base.mkdir();
        String data = new String(new URL(manifest).openStream().readAllBytes());
        ManifestJson manifests = gson.fromJson(data, ManifestJson.class);
        String assets = null;
        cache = new File(base, bridge.getMinecraftVersion());
        cache.mkdir();
        File sFile = new File(base, bridge.getMinecraftVersion() + "_manifest.json");
        if (!sFile.exists())
            for (Manifest manifest : manifests.versions) {
                System.out.println(manifest.id + "<===>" + bridge.getMinecraftVersion());
                if (manifest.id.startsWith(bridge.getMinecraftVersion())) {
                    System.out.println(gson.toJson(manifest));
                    String data2 = new String(new URL(manifest.url).openStream().readAllBytes());
                    Version versionJson = gson.fromJson(data2, Version.class);
                    System.out.println(versionJson.assetIndex.url);
                    assets = new String(new URL(versionJson.assetIndex.url).openStream().readAllBytes());
                    Util.write(sFile, assets);
                    break;
                }

            }
        else
            assets = Util.readFile(sFile);
        if (assets == null)
            return;

        Assets assetsObj = gson.fromJson(assets, Assets.class);
        assetsObj.objects.forEach((k, v) -> {
            boolean langParser = k.endsWith(".lang");
            if (k.startsWith("minecraft/lang/") && (k.endsWith(".json")) || langParser) {

                String name = k.substring(k.lastIndexOf("/") + 1, k.length() - 5);
                if (langParser)
                    name = name.toLowerCase();
                File finalFile = new File(cache, name + ".json");
                if (!finalFile.exists() && (packs.size() < 1 || packs.contains(name) || name.equals("en_gb")))
                    try {
                        logger.info("Downloading language : " + name);
                        String d = new String(
                                new URL("https://resources.download.minecraft.net/" + v.hash.substring(0, 2) + "/"
                                        + v.hash)
                                        .openStream().readAllBytes());
                        // We need to convert the output to JSON
                        if (langParser) {
                            String done = "{";
                            for (String line : d.split("\n")) {
                                if (line.contains("=")) {
                                    String[] items = line.split("=");
                                    done += "\"" + items[0] + "\":\"" + items[1] + "\",";
                                }
                            }
                            d = done.substring(0, done.length() - 1) + "}";
                        }
                        Util.write(finalFile, d);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

            }
        });
        conf = new File(base, "config.json");
        if (conf.exists()) {
            try {
                config = gson.fromJson(Util.readFile(conf), Config.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (config == null) {
            config = new Config();
            Util.write(conf, gson.toJson(config));
        }
        setLang(config.selected, false);

        // System.out.print(data);
    }

    public boolean has(String string) {
        return mappings.containsKey(string);
    }
}
