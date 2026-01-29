package tfar.collapsecycle;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

public class FabricEvents {

    public static final Event<CropGrowPre> CROP_GROW_PRE = EventFactory.createArrayBacked(CropGrowPre.class, callbacks ->
            (level, pos, state, def) -> {
                Result result = Result.DEFAULT;
                for (CropGrowPre callback : callbacks) {
                    result = callback.grow(level, pos, state, def);
                }
        return result;
    });

    public static final Event<BonemealUse> BONEMEAL_USE = EventFactory.createArrayBacked(BonemealUse.class,callbacks -> (level, pos, state, stack) -> {
        Pair<Boolean,Result> pair = BonemealUse.DEFAULT;
        for (BonemealUse bonemealUse:callbacks) {
            pair = bonemealUse.onApplyBonemeal(level,pos,state,stack);
        }
        return pair;
    });

    @FunctionalInterface
    public interface CropGrowPre {
        Result grow(Level level, BlockPos pos, BlockState state, boolean def);
    }

    @FunctionalInterface
    public interface BonemealUse {
        Pair<Boolean,Result> DEFAULT = Pair.of(false,Result.DEFAULT);
        Pair<Boolean,Result> onApplyBonemeal(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ItemStack stack);
    }
}
