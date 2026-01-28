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
        ModDatapackRegistryProvider provider = new ModDatapackRegistryProvider(packOutput,lookupProvider);
        generator.addProvider(true,provider);
        lookupProvider = provider.getRegistryProvider();
        generator.addProvider(true,new ModLangProvider(packOutput));
        generator.addProvider(true,new ModRecipeProvider(packOutput));
        generator.addProvider(true,new ModBlockstateProvider(packOutput,existingFileHelper));
        generator.addProvider(true,new ModItemModelProvider(packOutput,existingFileHelper));
        BlockTagsProvider blockTagsProvider = new ModBlockTagProvider(packOutput,lookupProvider,existingFileHelper);
        generator.addProvider(true,blockTagsProvider);
        generator.addProvider(true,new ModDamageTypeTagProvider(packOutput,lookupProvider,existingFileHelper));
    }
}
