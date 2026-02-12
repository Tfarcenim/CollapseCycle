package tfar.collapsecycle.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import tfar.collapsecycle.Constants;
import tfar.collapsecycle.init.ModBlocks;

public class ModBlockstateProvider extends BlockStateProvider {
    public ModBlockstateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Constants.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlockWithItem(ModBlocks.NULLSTONE.get(),cubeAll(ModBlocks.NULLSTONE.get()));
        simpleBlockWithItem(ModBlocks.DESTABILIZER.get(),models().cubeAll("destabilizer",mcLoc("block/lodestone_side")));
        flower(ModBlocks.SPARKFLOWER.get(),mcLoc("block/dandelion"));
        simpleBlock(ModBlocks.OVERWORLD_PORTAL.get(),models().getExistingFile(modLoc("block/overworld_portal")));
    }

    protected void flower(Block block, ResourceLocation texture) {
        simpleBlock(block,models().cross("sparkflower",texture));
        itemModels().singleTexture("sparkflower",mcLoc("item/generated"),"layer0",texture);
    }
}
