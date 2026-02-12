package tfar.collapsecycle.datagen.loot;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import tfar.collapsecycle.init.ModBlocks;

import java.util.List;
import java.util.Set;

public class ModBlockLoot extends BlockLootSubProvider {
    public ModBlockLoot() {
        super(Set.of(), FeatureFlags.VANILLA_SET);
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocks.CORRUPT.get());
        dropSelf(ModBlocks.DESTABILIZER.get());
        dropSelf(ModBlocks.NULLSTONE.get());
        dropSelf(ModBlocks.SPARKFLOWER.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return List.of(ModBlocks.CORRUPT.get(),ModBlocks.DESTABILIZER.get(),ModBlocks.NULLSTONE.get(),
                ModBlocks.SPARKFLOWER.get());
    }
}
