package tfar.collapsecycle.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import tfar.collapsecycle.init.ModBlockEntityTypes;

public class DestabilizerBlockEntity extends BlockEntity {
    public DestabilizerBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntityTypes.DESTABILIZER, pos, blockState);
    }
}
