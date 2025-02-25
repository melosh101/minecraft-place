package dk.milasholsting.place.commands;

import dk.milasholsting.place.Config;
import dk.milasholsting.place.Place;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PaletteCommand implements CommandExecutor, TabCompleter {

    final List<String> BlockList = Arrays.stream(Material.values()).filter(Material::isBlock).filter(Predicate.not(Material::isLegacy)).map(Enum::toString).toList();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(args.length == 0) {
           return false;
        }
        Place.logger.info(Arrays.toString(args));

        return switch (args[0].toLowerCase()) {
            case "add": {
                if (!args[1].isBlank()) {
                    if (args.length >= 3) {
                        var items = Arrays.copyOfRange(args, 3, args.length);
                        Arrays.stream(items).iterator().forEachRemaining(item -> Config.palette.add(item.toUpperCase()));
                        sender.sendMessage("Added " + items.length + " items to Palette");
                        yield true;
                    }

                    if (!Config.palette.contains(args[1].toUpperCase())) {
                        Config.palette.add(args[1].toUpperCase());
                        sender.sendMessage("added " + args[1] + " to the palette");
                        yield true;
                    }
                    sender.sendMessage(args[1], " is already in the palette");
                }
            }

            case "add_pattern": {
                if (!args[1].isBlank()) {

                    var foundBlocks = findRegexMatches(args[1]);
                    if (foundBlocks == null) {
                        sender.sendMessage("no blocks matched");
                        yield true;
                    }

                    foundBlocks.forEach(item -> Config.palette.add(item.toUpperCase()));
                    sender.sendMessage("added " + foundBlocks.toArray().length + " items to the palette");

                    yield true;
                }
            }
            case "remove": {
                if (!args[1].isBlank()) {
                    if (Config.palette.contains(args[1])) {
                        Config.palette.remove(args[1]);
                        sender.sendMessage("removed " + args[1] + " from the palette");
                        yield true;
                    }
                    sender.sendMessage(args[1], " is not in the palette");
                    yield true;
                }

            }

            case "list": {
                sender.sendMessage("Palette List: " + String.join(", ", Config.palette));
                yield true;
            }

            case "clear": {
                if (args[1].isBlank()) {
                    sender.sendMessage("please type '/palette clear confirm' if you are sure you want to clear the palette");
                    yield true;
                }
                if (args[1].equalsIgnoreCase("confirm")) {
                    Config.palette.clear();
                    sender.sendMessage("cleared palette");
                    yield true;
                }
            }

            case "toggle": {
                sender.sendMessage("WTH");
                Config.paletteEnabled = !Config.paletteEnabled;
                sender.sendMessage("palette toggled");
                yield true;
            }
            default:
                yield false;
        };
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        Place.logger.warning(Arrays.toString(args));
        if (args.length == 1) {
            return List.of("add", "add_pattern", "remove", "list", "clear", "toggle");
        }

        if (args.length == 2) {
            return switch (args[0].toLowerCase()) {
                case "add" -> {
                    List<String> list = new ArrayList<>();
                    StringUtil.copyPartialMatches(args[1], BlockList, list);
                    yield list;
                }

                case "remove" -> Config.palette.stream().toList();

                default -> List.of();
            };
        }
        return List.of();
    }

    List<String> findRegexMatches(String regex) {
        List<String> list = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        BlockList.forEach((block) -> {
            Matcher matcher = pattern.matcher(block);
            if(matcher.find()) {
                list.add(block.toUpperCase());
            }
        });
        return list;
    }

}
