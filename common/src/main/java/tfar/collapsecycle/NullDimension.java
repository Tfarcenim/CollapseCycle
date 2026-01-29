package tfar.collapsecycle;

import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.MiscOverworldPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.flat.FlatLayerInfo;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import net.minecraft.world.level.levelgen.placement.*;
import tfar.collapsecycle.init.ModBlocks;
import tfar.collapsecycle.init.ModFeatures;

import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

public class NullDimension {
    public static final String NAME = "nullzone";
    public static final ResourceLocation ID = CollapseCycle.id(NAME);
    public static final ResourceKey<Level> DIMENSION = ResourceKey.create(Registries.DIMENSION, ID);
    public static final ResourceKey<LevelStem> LEVEL_STEM = ResourceKey.create(Registries.LEVEL_STEM, ID);
    public static final ResourceKey<DimensionType> DIMENSION_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE,ID);
    public static final DimensionSpecialEffects SPECIAL_EFFECTS = new NullDimensionEffect(Float.NaN,true, DimensionSpecialEffects.SkyType.NONE,false,false);
    public static final ResourceKey<Biome> BIOME = ResourceKey.create(Registries.BIOME, CollapseCycle.id("nullzone"));

    public static void bootstrapType(BootstapContext<DimensionType> context) {
        context.register(DIMENSION_TYPE,new DimensionType(OptionalLong.of(6000),false,false,false,false,
                1,true,true,0,256,256,
                BlockTags.INFINIBURN_OVERWORLD, NullDimension.ID,0,
                new DimensionType.MonsterSettings(false, true, UniformInt.of(0, 7), 0)));
    }

    public static void bootstrapBiome(BootstapContext<Biome> context) {
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> worldCarvers = context.lookup(Registries.CONFIGURED_CARVER);

        BiomeGenerationSettings.Builder builder = new BiomeGenerationSettings.Builder(placedFeatures, worldCarvers);
        builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModFeatures.FLOWER_NULLZONE_PF);
        builder.addFeature(GenerationStep.Decoration.LAKES, MiscOverworldPlacements.LAKE_LAVA_SURFACE);
       Biome biome = ModFeatures.biome(false, 0.5F, 0.5F, new MobSpawnSettings.Builder(), builder, null);

        context.register(BIOME,biome);
    }

    public static void bootstrapStem(BootstapContext<LevelStem> context) {
        HolderGetter<Biome> holdergetter = context.lookup(Registries.BIOME);
        HolderGetter<DimensionType> holdergetterDim = context.lookup(Registries.DIMENSION_TYPE);
        FlatLevelGeneratorSettings flatLevelGeneratorSettings = new FlatLevelGeneratorSettings(
                Optional.empty(),holdergetter.getOrThrow(BIOME), List.of());
        flatLevelGeneratorSettings.setAddLakes();
        flatLevelGeneratorSettings.setDecoration();
        flatLevelGeneratorSettings.getLayersInfo().add(new FlatLayerInfo(1, Blocks.BEDROCK));
        flatLevelGeneratorSettings.getLayersInfo().add(new FlatLayerInfo(1, ModBlocks.NULLSTONE.get()));
        context.register(LEVEL_STEM,new LevelStem(holdergetterDim.getOrThrow(DIMENSION_TYPE),new FlatLevelSource(flatLevelGeneratorSettings)
        ));
    }
}
