package dk.milasholsting.place;

import com.sk89q.worldguard.WorldGuard;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;


public final class Place extends JavaPlugin {
    public static Place plugin;
    public static Logger logger;
    public static WorldGuard worldGuard;
    public static BukkitScheduler scheduler;
    public static HashMap<UUID, LocalDateTime> cooldowns = new HashMap<>();

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

        // schedule repeating task for updating the taskbar
        scheduler.scheduleSyncRepeatingTask(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                var cooldown = cooldowns.get(player.getUniqueId());
                Component msg;

                if (cooldown == null || cooldown.isAfter(LocalDateTime.now())) {
                    msg = Component.text("You can now place a block");
                    player.sendActionBar(msg);
                    return;
                }
                var timeLeft = Period.between(LocalDate.from(cooldown), LocalDate.now());
                msg = Component.text("You are on cooldown for" + timeLeft.normalized().toString() + " seconds");
                player.sendActionBar(msg);
            }
        }, 0L, 20L * Config.cooldown);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        logger.info("Plugin disabled");
        Config.save();
    }
}
