package tfar.collapsecycle.init;

import com.google.common.base.Suppliers;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import tfar.collapsecycle.block.DestabilizerBlock;
import tfar.collapsecycle.platform.Services;

import java.util.function.Supplier;

public class ModBlocks {
    public static final Supplier<Block> NULLSTONE = Services.PLATFORM.blockSupplier("nullstone",
           Suppliers.memoize(() -> new Block(BlockBehaviour.Properties.of().requiresCorrectToolForDrops())));

    public static final Supplier<Block> DESTABILIZER = Services.PLATFORM.blockSupplier("destabilizer",
            Suppliers.memoize(() -> new DestabilizerBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops())));

    public static final Supplier<FlowerBlock> SPARKFLOWER = Services.PLATFORM.blockSupplier("sparkflower",
            Suppliers.memoize(() -> new FlowerBlock(MobEffects.SATURATION,1200,BlockBehaviour.Properties.of().requiresCorrectToolForDrops())));

    public static void init() {

    }
}
