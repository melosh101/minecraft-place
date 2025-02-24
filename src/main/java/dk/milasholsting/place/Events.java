package dk.milasholsting.place;

import dk.milasholsting.place.events.onPlaceListener;

public class Events {

    public static void register() {
        Place.plugin.getServer().getPluginManager().registerEvents(new onPlaceListener(), Place.plugin);
    }
}
