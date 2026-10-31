package de.characterloading;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/** Simple JSON config: config/characterloading.json */
public class Config {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("characterloading.json");
    private static Config instance = new Config();

    public String character = "WEMMBU";
    public boolean enabled = true;
    public boolean inWorld = true;

    public static Config get() { return instance; }

    public Figure character() {
        try { return Figure.valueOf(character); } catch (Exception e) { return Figure.WEMMBU; }
    }

    public static void load() {
        try {
            if (Files.exists(FILE)) {
                Config c = GSON.fromJson(Files.readString(FILE), Config.class);
                if (c != null) instance = c;
            } else {
                save();
            }
        } catch (Exception e) {
            System.err.println("[characterloading] Could not read config: " + e);
        }
    }

    public static void save() {
        try {
            Files.writeString(FILE, GSON.toJson(instance));
        } catch (IOException e) {
            System.err.println("[characterloading] Could not save config: " + e);
        }
    }
}
