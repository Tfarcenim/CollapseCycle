package tfar.collapsecycle.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

public class ModDatagen {
    public static void gather(GatherDataEvent event){
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        generator.addProvider(true,new ModDatapackRegistryProvider(packOutput,lookupProvider));
        generator.addProvider(true,new ModLangProvider(packOutput));
        generator.addProvider(true,new ModRecipeProvider(packOutput));
        generator.addProvider(true,new ModBlockstateProvider(packOutput,existingFileHelper));
        BlockTagsProvider blockTagsProvider = new ModBlockTagProvider(packOutput,lookupProvider,existingFileHelper);
        generator.addProvider(true,blockTagsProvider);
    }
}
