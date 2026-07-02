package tfar.collapsecycle;

import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import tfar.collapsecycle.init.ModBlockEntityTypes;
import tfar.collapsecycle.init.ModSounds;
import tfar.collapsecycle.network.PacketHandler;

public class CollapseCycleFabric implements ModInitializer {

    @Override
    public void onInitialize() {

        // This method is invoked by the Fabric mod loader when it is ready
        // to load your mod. You can access Fabric and Common code in this
        // project.

        // Use Fabric to bootstrap the Common mod.
        ForgeConfigRegistry.INSTANCE.register(CollapseCycle.MOD_ID, ModConfig.Type.SERVER, CollapseCycleConfig.Server.SPEC);
        ForgeConfigRegistry.INSTANCE.register(CollapseCycle.MOD_ID, ModConfig.Type.CLIENT, CollapseCycleConfig.Client.SPEC);

        ServerTickEvents.START_WORLD_TICK.register(CollapseCycle::levelTick);
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> CollapseCycleCommands.register(commandDispatcher));
        CollapseCycle.init();

        FabricEvents.CROP_GROW_PRE.register((level, pos, state, def) -> {
            return !CollapseCycle.canUseBonemeal(level) ? Result.DEFAULT : Result.DENY;
        });

        FabricEvents.BONEMEAL_USE.register((level, pos, state, stack) -> {
            return CollapseCycle.canUseBonemeal(level) ? FabricEvents.BonemealUse.DEFAULT : Pair.of(true,Result.DEFAULT);
        });

        PacketHandler.registerPackets();
        Registry.register(BuiltInRegistries.SOUND_EVENT,CollapseCycle.id("collapse_start"),ModSounds.COLLAPSE_START);
        Registry.register(BuiltInRegistries.SOUND_EVENT,CollapseCycle.id("unnatural_collapse_start"),ModSounds.UNNATURAL_COLLAPSE_START);
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,CollapseCycle.id("destabilizer"), ModBlockEntityTypes.DESTABILIZER);
    }

    public static boolean onCropsGrowPre(Level level, BlockPos pos, BlockState state, boolean def) {
        Result result = FabricEvents.CROP_GROW_PRE.invoker().grow(level, pos, state, def);
        return result == Result.ALLOW || result == Result.DEFAULT && def;
    }

    public static int onApplyBonemeal(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ItemStack stack){
        Pair<Boolean,Result> pair = FabricEvents.BONEMEAL_USE.invoker().onApplyBonemeal(level, pos, state, stack);
        if (pair.getKey()) return -1;
        if (pair.getValue() == Result.ALLOW)
        {
            if (!level.isClientSide)
                stack.shrink(1);
            return 1;
        }
        return 0;
    }
}
