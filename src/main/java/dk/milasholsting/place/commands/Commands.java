package dk.milasholsting.place.commands;

import dk.milasholsting.place.Place;

@SuppressWarnings("DataFlowIssue")
public class Commands {
    public static void register() {
        PaintingCommand paintingCommand = new PaintingCommand();
        Place.plugin.getCommand("painting").setExecutor(paintingCommand);
        Place.plugin.getCommand("painting").setTabCompleter(paintingCommand);

        PaletteCommand paletteCommand = new PaletteCommand();
        Place.plugin.getCommand("palette").setExecutor(paletteCommand);
        Place.plugin.getCommand("palette").setTabCompleter(paletteCommand);

        BlacklistCommand blacklistCommand = new BlacklistCommand();
        Place.plugin.getCommand("blacklist").setExecutor(blacklistCommand);
        Place.plugin.getCommand("blacklist").setTabCompleter(blacklistCommand);
    }
}
