package tfar.collapsecycle;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.PrimaryLevelData;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.phys.Vec3;
import tfar.collapsecycle.network.S2CSetCollapseInfo;
import tfar.collapsecycle.platform.Services;

public class CollapseCycleCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal(CollapseCycle.MOD_ID)
                .then(Commands.literal("trigger")
                        .requires(stack -> stack.hasPermission(Commands.LEVEL_GAMEMASTERS))
                        .executes(CollapseCycleCommands::trigger))
                .then(Commands.literal("delete_overworld")
                        .requires(stack -> stack.hasPermission(Commands.LEVEL_ADMINS))
                        .executes(CollapseCycleCommands::deleteOverworld)
                )
                .then(Commands.literal("nullzone")
                        .requires(stack -> stack.hasPermission(Commands.LEVEL_GAMEMASTERS))
                        .executes(CollapseCycleCommands::nullzone)
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

    static int nullzone(CommandContext<CommandSourceStack> context) {
        CommandSourceStack commandSourceStack = context.getSource();
        MinecraftServer server = commandSourceStack.getServer();
        ServerLevel nullzoneLevel = server.getLevel(NullDimension.DIMENSION);
        ServerPlayer serverPlayer = commandSourceStack.getPlayer();

        Vec3 origin = CollapseCycle.nullzoneOrigin(nullzoneLevel);

        serverPlayer.teleportTo(nullzoneLevel,origin.x,origin.y,origin.z, 0, 0);
        Services.PLATFORM.sendToClient(new S2CSetCollapseInfo(false,CollapseCycleConfig.Server.TIME_LIMIT.get()),serverPlayer);
        return 1;
    }
}
