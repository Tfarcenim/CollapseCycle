package tfar.collapsecycle.datagen;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import tfar.collapsecycle.Constants;
import tfar.collapsecycle.init.ModItems;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        flatSingleLayer(ModItems.GRASSWAND.get(),"item/bamboo");
        flatSingleLayer(ModItems.COLLAPSE_CLOCK.get(),"item/clock_00");
    }



    private ItemModelBuilder flatSingleLayer(Item item, String texture) {
        String id = BuiltInRegistries.ITEM.getKey(item).getPath();
        return singleTexture(
                id,
                mcLoc("item/generated"),
                "layer0",
                new ResourceLocation(texture));
    }
}
