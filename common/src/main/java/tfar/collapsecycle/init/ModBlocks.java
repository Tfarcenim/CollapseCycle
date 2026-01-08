package tfar.collapsecycle.init;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import tfar.collapsecycle.platform.Services;

import java.util.function.Supplier;

public class ModBlocks {
    public static final Supplier<Block> NULLSTONE = Services.PLATFORM.blockSupplier("nullstone",
            () -> new Block(BlockBehaviour.Properties.of().requiresCorrectToolForDrops()));
}
