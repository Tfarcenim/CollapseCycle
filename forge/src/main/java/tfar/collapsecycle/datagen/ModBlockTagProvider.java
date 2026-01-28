package tfar.collapsecycle.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import tfar.collapsecycle.CollapseCycle;
import tfar.collapsecycle.Constants;
import tfar.collapsecycle.block.SparkFlowerBlock;
import tfar.collapsecycle.init.ModBlocks;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @org.jetbrains.annotations.Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.CORRUPT.get(),ModBlocks.DESTABILIZER.get(),ModBlocks.NULLSTONE.get());

        tag(BlockTags.NEEDS_IRON_TOOL).add(ModBlocks.CORRUPT.get());

        tag(BlockTags.SMALL_FLOWERS).add(ModBlocks.SPARKFLOWER.get());

        tag(SparkFlowerBlock.PLACEABLE_ON).addTag(BlockTags.DIRT).add(Blocks.FARMLAND,ModBlocks.NULLSTONE.get());
    }
}
