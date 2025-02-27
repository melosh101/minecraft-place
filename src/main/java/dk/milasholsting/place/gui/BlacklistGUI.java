package dk.milasholsting.place.gui;

import dk.milasholsting.place.Config;
import dk.milasholsting.place.Place;
import dk.milasholsting.place.Utils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.IntStream;

public class BlacklistGUI implements InventoryHolder {
    private final Inventory inventory;
    private int page = 0;
    // prevent any weird issues if blocks are added while ui is open
    private final List<List<String>> pages;

    public BlacklistGUI() {
        this.pages = Utils.getPages(Config.blacklist, 45);
        this.inventory = Place.plugin.getServer().createInventory(this, 54);
        initInventory();
    }
    private void initInventory() {
        addItemsFromBlacklist();
        ItemStack next;
        ItemStack prev;
        if (page > 0) {
            prev = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
            ItemMeta meta = prev.getItemMeta();
            meta.customName(Component.text("<-- Previous Page"));
            prev.setItemMeta(meta);
        } else {
            prev = new ItemStack(Material.RED_STAINED_GLASS_PANE);
            ItemMeta meta = prev.getItemMeta();
            meta.customName(Component.text("Already on first page"));
            prev.setItemMeta(meta);
        }

        if(pages.size() > page) {
            next = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
            ItemMeta meta = next.getItemMeta();
            meta.customName(Component.text("Next -->"));
            next.setItemMeta(meta);
        } else {
            next = new ItemStack(Material.RED_STAINED_GLASS_PANE);
            ItemMeta meta = next.getItemMeta();
            meta.customName(Component.text("You are on the last page"));
            next.setItemMeta(meta);
        }
        final ItemStack FILLER = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta fillerMeta = FILLER.getItemMeta();
        fillerMeta.customName(Component.text("Blacklist"));
        ItemStack sign = new ItemStack(Material.OAK_SIGN);
        ItemMeta meta = sign.getItemMeta();
        meta.customName(Component.text("Blacklist"));
        meta.lore(List.of(
                Component.text("You are on page" + (page + 1) + " of " + pages.size())
        ));
        sign.setItemMeta(meta);

        inventory.setItem(53, next);
        inventory.setItem(52, FILLER);
        inventory.setItem(51, FILLER);
        inventory.setItem(50, FILLER);
        inventory.setItem(49, sign);
        inventory.setItem(48, FILLER);
        inventory.setItem(47, FILLER);
        inventory.setItem(46, FILLER);
        inventory.setItem(45, prev);
    }
    private void addItemsFromBlacklist() {
        List<String> blocks = pages.get(page);
        IntStream.range(0, blocks.size()).forEach(i -> {
            Material mat = Material.getMaterial(blocks.get(i));
            if(mat == null) {
                return;
            }
            inventory.setItem(i, new ItemStack(mat, 0));
        });
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public static handleEvent() {

    }
}
