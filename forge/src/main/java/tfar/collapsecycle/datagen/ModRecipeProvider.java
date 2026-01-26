package tfar.collapsecycle.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import tfar.collapsecycle.init.ModBlocks;
import tfar.collapsecycle.init.ModItems;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.COLLAPSE_CLOCK.get())
                .define('c', Items.CLOCK)
                .define('o',Items.OBSERVER)
                .define('i',Items.IRON_INGOT)
                .define('t',Items.TINTED_GLASS)
                .define('C', ModBlocks.CORRUPT.get())
                .pattern(" o ")
                .pattern("ici")
                .pattern("CtC")
                .unlockedBy("has_corrupt",has(ModItems.CORRUPT.get()))
                .save(consumer);//(Craftable with a clock, corrupt, observer, 2 iron, and 1 tinted-glass)

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.GRASSWAND.get())
                .define('g', Items.GRASS_BLOCK)
                .define('b',Items.BLAZE_ROD)
                .define('a',Items.AMETHYST_SHARD)
                .pattern(" b ")
                .pattern("aga")
                .pattern(" b ")
                .unlockedBy("has_grass_block",has(Items.GRASS_BLOCK))
                .save(consumer);//Crafted with a grass block, 2 blaze rods, and 2 amethyst shards.
    }
}
