package dk.milasholsting.place;

import com.sk89q.worldguard.WorldGuard;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;


public final class Place extends JavaPlugin {
    public static Place plugin;
    public static Logger logger;
    public static WorldGuard worldGuard;
    public static BukkitScheduler scheduler;
    public static final HashMap<UUID, Instant> cooldowns = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        plugin.saveDefaultConfig();
        logger = plugin.getLogger();
        worldGuard = WorldGuard.getInstance();
        scheduler = Bukkit.getScheduler();
        Config.load();
        Commands.register();
        Events.register();
        logger.info("Plugin enabled");

        // schedule repeating task for updating the player actionbar
        // there might be a smarter or more efficient way of doing this, but this works
        scheduler.scheduleSyncRepeatingTask(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                var cooldown = cooldowns.get(player.getUniqueId());
                Component msg;
                if (cooldown == null ||cooldown.isBefore(Instant.now().minusSeconds(Config.cooldown))) {
                    msg = Component.text("You can now place a block").color(TextColor.color(0x3BAF30));
                    player.sendActionBar(msg);
                    return;
                }
                var timeLeft = Duration.between(Instant.now().minusSeconds(Config.cooldown), cooldown).toSeconds();
                msg = Component.text("You are on cooldown for ")
                        .append(Component.text(timeLeft).decorate(TextDecoration.BOLD).color(TextColor.color(0xA41E1A)))
                        .append(Component.text(" seconds"));
                player.sendActionBar(msg);
            }
        }, 0L, 20L);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        logger.info("Plugin disabled");
        Config.save();
    }
}
