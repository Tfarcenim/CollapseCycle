package tfar.collapsecycle.datagen;

import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import tfar.collapsecycle.CollapseCycle;
import tfar.collapsecycle.Constants;
import tfar.collapsecycle.init.ModBlocks;

public class ModBlockstateProvider extends BlockStateProvider {
    public ModBlockstateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Constants.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlockWithItem(ModBlocks.NULLSTONE.get(),cubeAll(ModBlocks.NULLSTONE.get()));
    }
}
