package tfar.collapsecycle.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;

public class CorruptBlock extends FallingBlock {
    public CorruptBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void onLand(Level level, BlockPos pos, BlockState state, BlockState replaceableState, FallingBlockEntity fallingBlock) {
        super.onLand(level, pos, state, replaceableState, fallingBlock);
        if (level.random.nextDouble() < 15/16d) {
            level.explode(null,pos.getX(),pos.getY(),pos.getZ(),4,false, Level.ExplosionInteraction.BLOCK);
        }
    }
}
