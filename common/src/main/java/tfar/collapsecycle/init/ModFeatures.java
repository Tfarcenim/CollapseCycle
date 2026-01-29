package tfar.collapsecycle.init;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.MiscOverworldFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.Music;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.LakeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.*;
import tfar.collapsecycle.CollapseCycle;

import javax.annotation.Nullable;

public class ModFeatures {
    public static final ResourceKey<PlacedFeature> FLOWER_NULLZONE_PF = ResourceKey.create(Registries.PLACED_FEATURE, CollapseCycle.id("flower_nullzone"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> FLOWER_NULLZONE = ResourceKey.create(Registries.CONFIGURED_FEATURE, CollapseCycle.id("flower_nullzone"));
    public static final ResourceKey<PlacedFeature> LAKE_WATER_PF = ResourceKey.create(Registries.PLACED_FEATURE, CollapseCycle.id("lake_water"));
    public static final ResourceKey<ConfiguredFeature<?,?>> LAKE_WATER = ResourceKey.create(Registries.CONFIGURED_FEATURE, CollapseCycle.id("lake_water"));

    public static void bootstrapConfiguredFeatures(BootstapContext<ConfiguredFeature<?, ?>> context) {
        FeatureUtils.register(context, FLOWER_NULLZONE,  Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.SPARKFLOWER.get()))));
        FeatureUtils.register(context, LAKE_WATER, Feature.LAKE, new LakeFeature.Configuration(BlockStateProvider.simple(Blocks.WATER.defaultBlockState()),
                BlockStateProvider.simple(Blocks.AIR.defaultBlockState())));
    }

    public static void bootstrapPlacedFeatures(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> holdergetter = context.lookup(Registries.CONFIGURED_FEATURE);
        Holder<ConfiguredFeature<?, ?>> holder22 = holdergetter.getOrThrow(FLOWER_NULLZONE);

        Holder<ConfiguredFeature<?, ?>> holder6 = holdergetter.getOrThrow(MiscOverworldFeatures.LAKE_LAVA);

        PlacementUtils.register(context, LAKE_WATER_PF, holder6, RarityFilter.onAverageOnceEvery(100), InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome());

        PlacementUtils.register(context, FLOWER_NULLZONE_PF, holder22, NoiseThresholdCountPlacement.of(-0.8D, 15, 4),
                RarityFilter.onAverageOnceEvery(32), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());
    }

    public static Biome biome(boolean hasPercipitation, float temperature, float downfall, MobSpawnSettings.Builder mobSpawnSettings, BiomeGenerationSettings.Builder generationSettings, @Nullable Music backgroundMusic) {
        return biome(hasPercipitation, temperature, downfall, 0xffffff, 0xffffff, null, null, mobSpawnSettings, generationSettings, backgroundMusic);
    }

    private static Biome biome(boolean hasPrecipitation, float temperature, float downfall, int waterColor, int waterFogColor,
                               @Nullable Integer grassColorOverride, @Nullable Integer foliageColorOverride,
                               MobSpawnSettings.Builder mobSpawnSettings, BiomeGenerationSettings.Builder generationSettings, @Nullable Music backgroundMusic) {
        BiomeSpecialEffects.Builder spoecialEffects = new BiomeSpecialEffects.Builder().waterColor(waterColor).waterFogColor(waterFogColor).fogColor(0x000000)
                .skyColor(0x000000).ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).backgroundMusic(backgroundMusic);
        if (grassColorOverride != null) {
            spoecialEffects.grassColorOverride(grassColorOverride);
        }


        if (foliageColorOverride != null) {
            spoecialEffects.foliageColorOverride(foliageColorOverride);
        }

        return new Biome.BiomeBuilder().hasPrecipitation(hasPrecipitation).temperature(temperature).downfall(downfall)
                .specialEffects(spoecialEffects.build()).mobSpawnSettings(mobSpawnSettings.build())
                .generationSettings(generationSettings.build()).build();
    }

    protected static int calculateSkyColor(float temperature) {
        float $$1 = temperature / 3.0F;
        $$1 = Mth.clamp($$1, -1.0F, 1.0F);
        return Mth.hsvToRgb(0.62222224F - $$1 * 0.05F, 0.5F + $$1 * 0.1F, 1.0F);
    }
}
