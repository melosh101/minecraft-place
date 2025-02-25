package dk.milasholsting.place.commands;

import dk.milasholsting.place.Config;
import dk.milasholsting.place.Place;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.List;

public class PaintingCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(args.length == 0) {
            return false;
        }

        return switch(args[0].toLowerCase()) {
            case "set_region" : {
                if(!args[1].isBlank()) {
                    Config.region = args[1];
                    yield true;
                }
                yield false;
            }

            case "get_region" : {
                sender.sendMessage("Place is configured to look for region " + Config.region);
                yield true;
            }

            case "reset_cooldown": {
                if(args.length != 2 || args[1].isBlank()) {
                    if(!(sender instanceof Player player)) {
                        sender.sendMessage("You must be a player to use this command. specify a player name if used from console.");
                        yield true;
                    }

                    Place.cooldowns.put(player.getUniqueId(), Instant.now());
                    yield true;
                }

                Player player = Place.plugin.getServer().getPlayer(args[1]);
                if(player == null) {
                    sender.sendMessage("Player not found.");
                    yield true;
                }
                Place.cooldowns.put(player.getUniqueId(), Instant.now());
                yield true;

            }

            case "reset_all_cooldowns": {
                Place.cooldowns.clear();
                sender.sendMessage("All cooldowns have been reset.");
                yield true;
            }

            case "set_cooldown": {
                if(args.length != 2 || args[1].isBlank()) {
                    sender.sendMessage("You must specify a cooldown in seconds.");
                    yield true;
                }

                try {
                    long cooldown = Long.parseLong(args[1]);
                    sender.sendMessage("Setting cooldown to " + cooldown);
                    Config.cooldown = cooldown;
                    yield true;
                } catch(NumberFormatException e) {
                    sender.sendMessage("You must specify a cooldown in seconds.");
                    yield true;
                }
            }



            default: yield false;
        };

    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(args.length == 1) {
            return List.of("set_region", "get_region", "config", "reset_cooldown", "reset_all_cooldowns", "set_cooldown");
        }

        if(args.length == 2) {
            return switch (args[0].toLowerCase()) {
                case "config": {
                    yield List.of("save", "revert");
                }
                case "reset_cooldown": {
                    yield Place.plugin.getServer().getOnlinePlayers().stream().map(Player::getName).toList();
                }

                case "set_cooldown": {
                    yield List.of(Config.cooldown.toString());
                }
                default:
                    yield List.of();
            };
        }

        return List.of();
    }
}
