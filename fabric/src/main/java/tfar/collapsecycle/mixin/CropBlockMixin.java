package tfar.collapsecycle.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import tfar.collapsecycle.CollapseCycleFabric;

@Mixin(CropBlock.class)
public abstract class CropBlockMixin {

    @Shadow public abstract int getAge(BlockState state);

    @Shadow public abstract int getMaxAge();

    @Shadow
    protected static float getGrowthSpeed(Block block, BlockGetter level, BlockPos pos) {
        return 0;
    }

    @Shadow public abstract BlockState getStateForAge(int age);

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.getRawBrightness(pos, 0) >= 9) {
            int i = this.getAge(state);
            if (i < this.getMaxAge()) {
                float f = getGrowthSpeed((Block) (Object)this, level, pos);
                boolean grown = random.nextInt((int)(25.0F / f) + 1) == 0;
                if (CollapseCycleFabric.onCropsGrowPre(level, pos, state, grown)) {
                    level.setBlock(pos, this.getStateForAge(i + 1), 2);
                }
            }
        }
    }
}
