package tfar.collapsecycle.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import tfar.collapsecycle.CollapseCycle;
import tfar.collapsecycle.init.ModDamageSource;

import java.util.concurrent.CompletableFuture;

public class ModDamageTypeTagProvider extends DamageTypeTagsProvider {


    public ModDamageTypeTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, CollapseCycle.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(DamageTypeTags.BYPASSES_ARMOR).add(ModDamageSource.COLLAPSE);
        tag(DamageTypeTags.BYPASSES_RESISTANCE).add(ModDamageSource.COLLAPSE);

    }
}
