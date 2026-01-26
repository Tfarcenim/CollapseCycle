package tfar.collapsecycle;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.flat.FlatLayerInfo;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import tfar.collapsecycle.init.ModBlocks;

import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

public class NullDimension {
    public static final String NAME = "nullzone";
    public static final ResourceLocation ID = CollapseCycle.id(NAME);
    public static final ResourceKey<Level> DIMENSION = ResourceKey.create(Registries.DIMENSION, ID);
    public static final ResourceKey<LevelStem> LEVEL_STEM = ResourceKey.create(Registries.LEVEL_STEM, ID);
    public static final ResourceKey<DimensionType> DIMENSION_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE,ID);

    public static void bootstrapType(BootstapContext<DimensionType> context) {
        context.register(DIMENSION_TYPE,new DimensionType(OptionalLong.of(6000),true,false,false,false,
                1,true,true,0,256,256,
                BlockTags.INFINIBURN_OVERWORLD, BuiltinDimensionTypes.OVERWORLD_EFFECTS,.1f,
                new DimensionType.MonsterSettings(false, true, UniformInt.of(0, 7), 0)));
    }

    public static void bootstrapStem(BootstapContext<LevelStem> context) {
        HolderGetter<Biome> holdergetter = context.lookup(Registries.BIOME);
        HolderGetter<DimensionType> holdergetterDim = context.lookup(Registries.DIMENSION_TYPE);
        FlatLevelGeneratorSettings flatLevelGeneratorSettings = new FlatLevelGeneratorSettings(
                Optional.empty(),holdergetter.getOrThrow(Biomes.THE_VOID), List.of());
        flatLevelGeneratorSettings.getLayersInfo().add(new FlatLayerInfo(1, Blocks.BEDROCK));
        flatLevelGeneratorSettings.getLayersInfo().add(new FlatLayerInfo(1, ModBlocks.NULLSTONE.get()));
        context.register(LEVEL_STEM,new LevelStem(holdergetterDim.getOrThrow(DIMENSION_TYPE),new FlatLevelSource(flatLevelGeneratorSettings)
        ));
    }
}
