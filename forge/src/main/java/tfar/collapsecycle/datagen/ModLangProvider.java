package tfar.collapsecycle.datagen;

import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;
import tfar.collapsecycle.CollapseCycle;
import tfar.collapsecycle.init.ModBlocks;
import tfar.collapsecycle.init.ModItems;

public class ModLangProvider extends LanguageProvider {
    public ModLangProvider(PackOutput output) {
        super(output, CollapseCycle.MOD_ID,"en_us");
    }

    @Override
    protected void addTranslations() {
        addBlock(ModBlocks.DESTABILIZER,"Destabilizer");
        addBlock(ModBlocks.NULLSTONE,"Nullstone");
        addBlock(ModBlocks.SPARKFLOWER,"Sparkflower");
        addItem(ModItems.COLLAPSE_CLOCK,"Collapse Clock");
        addItem(ModItems.CORRUPT,"Corrupt");
        addItem(ModItems.GRASSWAND,"Grasswand");
    }
}
