package tfar.collapsecycle;

import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.SurfaceRuleData;
import net.minecraft.data.worldgen.placement.MiscOverworldPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.*;
import tfar.collapsecycle.init.ModBlocks;
import tfar.collapsecycle.init.ModFeatures;

import java.util.OptionalLong;

public class NullDimension {
    public static final String NAME = "nullzone";
    public static final ResourceLocation ID = CollapseCycle.id(NAME);
    public static final ResourceKey<Level> DIMENSION = ResourceKey.create(Registries.DIMENSION, ID);
    public static final ResourceKey<LevelStem> LEVEL_STEM = ResourceKey.create(Registries.LEVEL_STEM, ID);
    public static final ResourceKey<DimensionType> DIMENSION_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE,ID);
    public static final DimensionSpecialEffects SPECIAL_EFFECTS = new NullDimensionEffect(Float.NaN,true, DimensionSpecialEffects.SkyType.NONE,false,false);
    public static final ResourceKey<Biome> BIOME = ResourceKey.create(Registries.BIOME, CollapseCycle.id("nullzone"));
    public static final ResourceKey<NoiseGeneratorSettings> NOISE_SETTINGS = ResourceKey.create(Registries.NOISE_SETTINGS, CollapseCycle.id("nullzone"));

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
        Holder.Reference<Biome> biome = holdergetter.getOrThrow(BIOME);
        HolderGetter<DimensionType> holdergetterDim = context.lookup(Registries.DIMENSION_TYPE);
        //FlatLevelGeneratorSettings flatLevelGeneratorSettings = new FlatLevelGeneratorSettings(
         //       Optional.empty(),biome, List.of());
        //flatLevelGeneratorSettings.setAddLakes();
        //flatLevelGeneratorSettings.setDecoration();

        //List<FlatLayerInfo> layersInfo = flatLevelGeneratorSettings.getLayersInfo();

        //layersInfo.add(new FlatLayerInfo(1, Blocks.BEDROCK));
        //layersInfo.add(new FlatLayerInfo(3, ModBlocks.NULLSTONE.get()));
       // layersInfo.add(new FlatLayerInfo(1, Blocks.GRASS_BLOCK));

        BiomeSource biomeSource = new FixedBiomeSource(biome);
        HolderGetter<NoiseGeneratorSettings> noiseRegistry =  context.lookup(Registries.NOISE_SETTINGS);
        Holder<NoiseGeneratorSettings> holder = noiseRegistry.getOrThrow(NOISE_SETTINGS);
        ChunkGenerator chunkGenerator = new NoiseBasedChunkGenerator(biomeSource,holder);
        context.register(LEVEL_STEM,new LevelStem(holdergetterDim.getOrThrow(DIMENSION_TYPE),chunkGenerator));
    }

    public static void bootstrapNoiseGeneratorSettings(BootstapContext<NoiseGeneratorSettings> context) {
        context.register(NOISE_SETTINGS,new NoiseGeneratorSettings(NoiseSettings.OVERWORLD_NOISE_SETTINGS,
                ModBlocks.NULLSTONE.get().defaultBlockState(), Blocks.WATER.defaultBlockState(),
                NoiseRouterData.overworld(context.lookup(Registries.DENSITY_FUNCTION), context.lookup(Registries.NOISE), false, false),
                SurfaceRules.state(ModBlocks.NULLSTONE.get().defaultBlockState()), new OverworldBiomeBuilder().spawnTarget(), 63, false,
                true, true, false));
    }
}
