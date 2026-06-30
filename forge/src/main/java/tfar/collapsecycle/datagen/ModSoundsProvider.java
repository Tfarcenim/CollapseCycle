package tfar.collapsecycle.datagen;

import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinitionsProvider;
import tfar.collapsecycle.CollapseCycle;
import tfar.collapsecycle.init.ModSounds;

public class ModSoundsProvider extends SoundDefinitionsProvider {
    protected ModSoundsProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, CollapseCycle.MOD_ID, helper);
    }

    @Override
    public void registerSounds() {
       add(ModSounds.COLLAPSE_START,definition().with(sound(ModSounds.COLLAPSE_START.getLocation())));
        add(ModSounds.UNNATURAL_COLLAPSE_START,definition().with(sound(ModSounds.UNNATURAL_COLLAPSE_START.getLocation())));
    }
}
