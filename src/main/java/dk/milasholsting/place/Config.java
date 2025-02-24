package dk.milasholsting.place;

import java.util.ArrayList;

public class Config {
    public static String region = "painting";
    public static Long cooldown = 6000L;
    public static ArrayList<String> palette = new ArrayList<String>();
    public static boolean paletteEnabled = false;

    public static void load() {
        region = Place.plugin.getConfig().getString("region", region);
        cooldown = Place.plugin.getConfig().getLong("cooldown", cooldown);
        paletteEnabled = Place.plugin.getConfig().getBoolean("palette.enabled", paletteEnabled);
        palette = (ArrayList<String>) Place.plugin.getConfig().getList("palette.blocks");
    }

    public static void save() {
        Place.plugin.getConfig().set("region", region);
        Place.plugin.getConfig().set("cooldown", cooldown);
        Place.plugin.saveConfig();
    }
}
