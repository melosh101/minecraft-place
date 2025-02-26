package dk.milasholsting.place.events;

import dk.milasholsting.place.Place;

public class Events {

    public static void register() {
        Place.plugin.getServer().getPluginManager().registerEvents(new onPlaceListener(), Place.plugin);
    }
}
