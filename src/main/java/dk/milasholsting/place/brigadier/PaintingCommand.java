package dk.milasholsting.place.brigadier;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import dk.milasholsting.place.Config;
import dk.milasholsting.place.Place;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.List;

@SuppressWarnings({"UnstableApiUsage", "SameReturnValue", "DataFlowIssue"})
public class PaintingCommand {
    public static LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("painting")
                .requires(sender -> sender.getExecutor().hasPermission("place.commands.painting"))
                .then(
                        Commands.literal("set_region")
                                .requires(sender -> sender.getExecutor().hasPermission("place.commands.painting.set_region"))
                                .then(
                                        Commands.argument("region", StringArgumentType.word())
                                                .executes(PaintingCommand::set_region)
                                )
                ).then(
                        Commands.literal("get_region")
                                .requires(sender -> sender.getExecutor().hasPermission("place.commands.painting.get_region"))
                                .executes(PaintingCommand::get_region)

                ).then(
                        Commands.literal("config")
                                .requires(sender -> sender.getExecutor().hasPermission("place.commands.painting.config"))
                                .then(Commands.literal("save").executes((context)-> {
                                    Config.save();
                                    context.getSource().getExecutor().sendMessage("Config saved");
                                    return Command.SINGLE_SUCCESS;
                                }))
                                .then(Commands.literal("revert")
                                        .then(
                                                Commands.argument("rest", StringArgumentType.string())
                                                        .suggests((ctx, builder) -> builder.suggest("confirm", MessageComponentSerializer.message().serialize(
                                                                MiniMessage.miniMessage().deserialize("<red>THIS WILL REVERT THE CONFIG TO LAST SAVE")
                                                        )).buildFuture())
                                                        .executes((context) -> {
                                                            CommandSender sender = context.getSource().getExecutor();
                                                            String rest = StringArgumentType.getString(context, "rest");
                                                            if(rest.equals("confirm")) {
                                                                Config.save();
                                                                sender.sendMessage(Component.text("The config has been reverted successfully!").color(TextColor.color(0xA624)));
                                                            }
                                                            return Command.SINGLE_SUCCESS;
                                                        })
                                        ).executes((context) -> {
                                            context.getSource().getExecutor().sendMessage("please confirm by typing /painting config revert confirm");
                                            return Command.SINGLE_SUCCESS;
                                        })
                                )
                ).then(
                        Commands.literal("set_cooldown")
                                .requires(sender -> sender.getExecutor().hasPermission("place.commands.painting.set_cooldown"))
                                .then(
                                Commands.argument("seconds", LongArgumentType.longArg(0))
                                        .executes(PaintingCommand::set_cooldown)
                        )
                ).then(
                        Commands.literal("reset_cooldown")
                                .requires(sender -> sender.getExecutor().hasPermission("place.commands.painting.reset_cooldowns"))
                                .then(
                                    Commands.argument("player", ArgumentTypes.player()).executes(PaintingCommand::reset_cooldown)
                        ).executes(PaintingCommand::reset_cooldown)
                ).build();
    }


    public static int set_region(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getExecutor();
        String region = StringArgumentType.getString(context, "region");
        if(sender instanceof Player) {
            RegionContainer container = Place.worldGuard.getPlatform().getRegionContainer();
            World weWorld = BukkitAdapter.adapt(context.getSource().getLocation().getWorld());
            RegionManager manager = container.get(weWorld);

            assert manager != null;
            ProtectedRegion pRegion = manager.getRegion(region);
            if(pRegion == null) {
                sender.sendMessage("Could not find region '" + region + "'");
                return Command.SINGLE_SUCCESS;
            }
            Config.region = pRegion.getId();
            sender.sendMessage("Region set to" + pRegion.getId());
        }
        return Command.SINGLE_SUCCESS;
    }

    @SuppressWarnings("SameReturnValue")
    public static int get_region(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getExecutor();

        sender.sendMessage("Place is configured to look for region " + Config.region);
        return Command.SINGLE_SUCCESS;
    }

    @SuppressWarnings("SameReturnValue")
    private static int set_cooldown(CommandContext<CommandSourceStack> context)  {
        CommandSender sender = context.getSource().getExecutor();
        Config.cooldown = (LongArgumentType.getLong(context, "seconds"));
        sender.sendMessage("Cooldown set to " + Config.cooldown + " seconds");
        return Command.SINGLE_SUCCESS;
    }

    @SuppressWarnings("SameReturnValue")
    private static int reset_cooldown(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getExecutor();
        final Player player = (Player) sender;
        List<Player> targets;
        try {
            final PlayerSelectorArgumentResolver targetResolver = context.getArgument("player", PlayerSelectorArgumentResolver.class);
             targets = targetResolver.resolve(context.getSource());
        } catch (IllegalArgumentException | CommandSyntaxException e) {
            targets = List.of(player);
        }
        targets.forEach(target -> {
            Place.cooldowns.put(target.getUniqueId(), Instant.MIN);
            if(target.getUniqueId().equals(player.getUniqueId())) {
                sender.sendMessage("you reset your cooldown");
            }else {
                sender.sendMessage("your cooldown was reset by " + player.getName());
            }
        });
        return Command.SINGLE_SUCCESS;
    }

}
