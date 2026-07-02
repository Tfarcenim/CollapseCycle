package tfar.collapsecycle.init;

import net.minecraft.world.level.block.entity.BlockEntityType;
import tfar.collapsecycle.block.DestabilizerBlockEntity;

public class ModBlockEntityTypes {
    public static final BlockEntityType<DestabilizerBlockEntity> DESTABILIZER = BlockEntityType.Builder.of(DestabilizerBlockEntity::new
            ,ModBlocks.DESTABILIZER.get()).build(null);
}
