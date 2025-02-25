package dk.milasholsting.place;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;

public class Config {
    public static String region = "painting";
    public static Long cooldown = 6000L;
    public static final HashSet<String> palette = new HashSet<>();
    public static final HashSet<String> blacklist = new HashSet<>();
    public static boolean paletteEnabled = false;

    public static void load() {
        region = Place.plugin.getConfig().getString("region", region);
        cooldown = Place.plugin.getConfig().getLong("cooldown", cooldown);
        paletteEnabled = Place.plugin.getConfig().getBoolean("palette.enabled", paletteEnabled);
        parsePaletteToSet(Place.plugin.getConfig().getList("palette.blocks"));
        parseBlacklistToSet(Place.plugin.getConfig().getList("palette.blacklist"));
    }

    public static void save() {
        Place.plugin.getConfig().set("region", region);
        Place.plugin.getConfig().set("cooldown", cooldown);
        Place.plugin.getConfig().set("palette.enabled", paletteEnabled);
        Place.plugin.getConfig().set("palette.blocks", palette.toArray());
        Place.plugin.getConfig().set("palette.blacklist", blacklist.toArray());
        Place.plugin.saveConfig();
    }

    public static void parsePaletteToSet(@Nullable List<?> input) {
        if(input == null) return;
        input.forEach((block) -> palette.add(block.toString().toLowerCase()));
    }
    public static void parseBlacklistToSet(@Nullable List<?> input) {
        if(input == null) return;
        input.forEach((block) -> blacklist.add(block.toString().toLowerCase()));
    }

}
