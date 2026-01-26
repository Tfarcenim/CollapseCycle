package tfar.collapsecycle.init;

import com.google.common.base.Suppliers;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import tfar.collapsecycle.block.CorruptBlock;
import tfar.collapsecycle.block.DestabilizerBlock;
import tfar.collapsecycle.platform.Services;

import java.util.function.Supplier;

public class ModBlocks {
    public static final Supplier<Block> NULLSTONE = Services.PLATFORM.blockSupplier("nullstone",
           Suppliers.memoize(() -> new Block(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F))));

    public static final Supplier<Block> DESTABILIZER = Services.PLATFORM.blockSupplier("destabilizer",
            Suppliers.memoize(() -> new DestabilizerBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops())));

    public static final Supplier<Block> CORRUPT = Services.PLATFORM.blockSupplier("corrupt",
            Suppliers.memoize(() -> new CorruptBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops())));

    public static final Supplier<FlowerBlock> SPARKFLOWER = Services.PLATFORM.blockSupplier("sparkflower",
            Suppliers.memoize(() -> new FlowerBlock(MobEffects.SATURATION,1200,BlockBehaviour.Properties.of()
                    .lightLevel(s -> 14)
                    .mapColor(MapColor.PLANT).noCollission().instabreak().sound(SoundType.GRASS)
                    .offsetType(BlockBehaviour.OffsetType.XZ).pushReaction(PushReaction.DESTROY)
            )));

    public static void init() {

    }
}
