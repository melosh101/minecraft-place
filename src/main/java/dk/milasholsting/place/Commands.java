package dk.milasholsting.place;

import dk.milasholsting.place.commands.TestCommand;

public class Commands {
    public static void register() {
        TestCommand testCommand = new TestCommand();
        Place.plugin.getCommand("/test").setTabCompleter(testCommand);
        Place.plugin.getCommand("/test").setExecutor(testCommand);
    }
}
