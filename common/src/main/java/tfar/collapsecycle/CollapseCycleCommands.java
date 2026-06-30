package tfar.collapsecycle;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.PrimaryLevelData;
import net.minecraft.world.level.storage.ServerLevelData;

public class CollapseCycleCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal(CollapseCycle.MOD_ID)
                .then(Commands.literal("trigger")
                        .requires(stack -> stack.hasPermission(Commands.LEVEL_GAMEMASTERS))
                        .executes(CollapseCycleCommands::trigger))
                .then(Commands.literal("delete")
                        .requires(stack -> stack.hasPermission(Commands.LEVEL_ADMINS))
                        .executes(CollapseCycleCommands::deleteOverworld)
                )
        );
    }

    static int deleteOverworld(CommandContext<CommandSourceStack> context) {
        CommandSourceStack commandSourceStack = context.getSource();
        if (commandSourceStack.getLevel().dimension() == NullDimension.DIMENSION) {
            MinecraftServer server = commandSourceStack.getServer();
            SpaceTimeManager.reset(server);
            return 1;
        }
        return 0;
    }

    static int trigger(CommandContext<CommandSourceStack> context) {
        CommandSourceStack commandSourceStack = context.getSource();
        MinecraftServer server = commandSourceStack.getServer();
        ServerLevelData serverLevelData = server.getWorldData().overworldData();
        if (serverLevelData instanceof PrimaryLevelData primaryLevelData) {
            primaryLevelData.setGameTime(CollapseCycleConfig.Server.TIME_LIMIT.get());
        }
        return 1;
    }
}
