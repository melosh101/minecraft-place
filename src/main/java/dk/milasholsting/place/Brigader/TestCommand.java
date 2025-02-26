package dk.milasholsting.place.Brigader;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestCommand {

    public static LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("test")
                .then(
                        Commands.literal("test1").executes(TestCommand::test1)
                )
                .then(
                        Commands.argument("number", IntegerArgumentType.integer(0, 100))
                                .executes(TestCommand::test2)
                ).build();
    }

    public static int test1(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getSender();
        sender.sendMessage("Hello World!");
        return Command.SINGLE_SUCCESS;
    }

    public static int test2(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getSender();
        int arg1 = IntegerArgumentType.getInteger(context, "number");
        sender.sendMessage("Hello World!" + arg1 * 10);
        return Command.SINGLE_SUCCESS;
    }

}
