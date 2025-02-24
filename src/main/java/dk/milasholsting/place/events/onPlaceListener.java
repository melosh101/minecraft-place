package dk.milasholsting.place.events;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import dk.milasholsting.place.Config;
import dk.milasholsting.place.Place;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

public class onPlaceListener implements Listener {

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        RegionContainer container = Place.worldGuard.getPlatform().getRegionContainer();

        World world = (World) event.getBlock().getWorld();
        RegionManager manager = container.get(world);

        assert manager != null;
        var region = manager.getRegion(Config.region);

        BlockVector3 blockLoc = new BlockVector3(event.getBlock().getX(), event.getBlock().getY(), event.getBlock().getZ());
        assert region != null;
        if (region.contains(blockLoc)) {
            event.setCancelled(true);
            if (Config.paletteEnabled && !isPartOfPalette(event.getBlock())) {
                event.getPlayer().sendMessage("Block is not part of the palette");
                return;
            }
            Block block = event.getBlock().getWorld().getBlockAt(blockLoc.x(), blockLoc.y(), blockLoc.z());

            block.setType(event.getBlock().getType());
            LocalDateTime date = LocalDateTime.now();
            Place.cooldowns.put(event.getPlayer().getUniqueId(), date.plusMinutes(5));
            event.getPlayer().sendMessage("you placed a block, you can place another in" + Period.between(LocalDate.from(date), LocalDate.from(date.plusMinutes(5))));
        }
    }

    public boolean isPartOfPalette(Block block) {
        return Config.palette.contains(block.getType().translationKey());
    }
}
