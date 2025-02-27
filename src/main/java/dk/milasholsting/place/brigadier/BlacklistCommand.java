package dk.milasholsting.place.brigadier;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dk.milasholsting.place.Config;
import dk.milasholsting.place.gui.BlacklistGUI;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings({"UnstableApiUsage", "SameReturnValue"})
public class BlacklistCommand {

    public static LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("blacklist")
                .then(Commands.literal("add")
                        .then(Commands.argument("block", ArgumentTypes.blockState())
                                .executes(BlacklistCommand::blacklist_add)
                        )
                )
                .then(Commands.literal("add_pattern")
                        .then(Commands.argument("regex", argument.ArgumentTypes.regex)
                                .executes(BlacklistCommand::blacklist_add_pattern)
                        )
                )
                .then(Commands.literal("remove")
                        .then(Commands.argument("block", ArgumentTypes.blockState())
                                .executes(BlacklistCommand::blacklist_remove)
                        )
                )
                .then(Commands.literal("list")
                        .executes(BlacklistCommand::list)
                )
                .then(Commands.literal("clear")
                        .executes(context -> {
                            Config.blacklist.clear();
                            return Command.SINGLE_SUCCESS;
                        })
                ).build();
    }

    private static int list(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getSender();
        if(!(sender instanceof Player)) {
            sender.sendMessage(String.join(",", Config.blacklist));
            return Command.SINGLE_SUCCESS;
        }
        Player player = (Player) sender;

        BlacklistGUI blacklistGUI = new BlacklistGUI();
        player.openInventory(blacklistGUI.getInventory());
        return Command.SINGLE_SUCCESS;
    }

    private static int blacklist_remove(CommandContext<CommandSourceStack> context) {
        return 0;
    }

    private static int blacklist_add_pattern(CommandContext<CommandSourceStack> context) {
        return 0;
    }

    private static int blacklist_add(CommandContext<CommandSourceStack> context) {
        return 0;
    }


}
