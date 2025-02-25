package dk.milasholsting.place.events;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import dk.milasholsting.place.Config;
import dk.milasholsting.place.Place;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.time.Duration;
import java.time.Instant;

public class onPlaceListener implements Listener {

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        RegionContainer container = Place.worldGuard.getPlatform().getRegionContainer();

        World world = block.getWorld();
        RegionManager manager = container.get(BukkitAdapter.adapt(world));

        assert manager != null;
        var region = manager.getRegion(Config.region);

        BlockVector3 blockLoc = new BlockVector3(block.getX(), block.getY(), block.getZ());
        assert region != null;
        if (region.contains(blockLoc)) {
            event.setCancelled(true);
            if (Config.paletteEnabled && !isPartOfPalette(block)) {
                player.sendMessage("Block is not part of the palette");
                return;
            }
            if (isBlacklistedBlock(block)) {
                player.sendMessage("Block is blacklisted");
                return;
            }
            Instant cooldown = Place.cooldowns.get(player.getUniqueId());
            // if (cooldown != null && Instant.now().plusSeconds(Config.cooldown).isBefore(cooldown)) {
            if (cooldown != null && cooldown.isAfter(Instant.now().minusSeconds(Config.cooldown))) {
                player.sendMessage("you are still on a cooldown, please wait another " + Duration.between(Instant.now().minusSeconds(Config.cooldown), cooldown).toSeconds() + " seconds");
                return;
            }
            Block targetBlock = block.getWorld().getBlockAt(blockLoc.x(), blockLoc.y() - 1, blockLoc.z());
            player.sendMessage(block.getType().toString());
            targetBlock.setType(block.getType());
            Place.cooldowns.put(player.getUniqueId(), Instant.now());
            player.sendMessage("you placed a block, you can place another in " + Config.cooldown + " seconds");
        }
    }

    public boolean isPartOfPalette(Block block) {
        return Config.palette.contains(block.getType().toString());
    }

    public boolean isBlacklistedBlock(Block block) {
        return Config.blacklist.contains(block.getType().toString());
    }
}
