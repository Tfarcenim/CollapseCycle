package tfar.collapsecycle.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import tfar.collapsecycle.Constants;
import tfar.collapsecycle.NullDimension;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModDatapackRegistryProvider extends DatapackBuiltinEntriesProvider {

    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.DIMENSION_TYPE, NullDimension::bootstrapType)
            .add(Registries.LEVEL_STEM, NullDimension::bootstrapStem);

    public ModDatapackRegistryProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(Constants.MOD_ID));
    }
}
