package tfar.collapsecycle.init;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.structures.DesertPyramidStructure;
import net.minecraft.world.level.levelgen.structure.structures.JungleTempleStructure;
import tfar.collapsecycle.CollapseCycle;
import tfar.collapsecycle.NullDimension;
import tfar.collapsecycle.levelgen.DestabilizerStructure;
import tfar.collapsecycle.levelgen.OneTimeStructure;
import tfar.collapsecycle.levelgen.OneTimeStructurePlacement;

import java.util.Map;

public class ModStructures {
    public static final ResourceKey<Structure> DESTABILIZER = createKey("destabilizer");
    public static final ResourceKey<Structure> NULLZONE_HUB = createKey("nullzone_hub");

    private static ResourceKey<Structure> createKey(String name) {
        return ResourceKey.create(Registries.STRUCTURE, CollapseCycle.id(name));
    }

    public static void bootstrap(BootstapContext<Structure> context) {
        HolderGetter<Biome> holdergetter = context.lookup(Registries.BIOME);
        context.register(DESTABILIZER, new DestabilizerStructure(structure(holdergetter.getOrThrow(BiomeTags.IS_OVERWORLD),
                TerrainAdjustment.NONE)));
        context.register(NULLZONE_HUB, new OneTimeStructure(structure(HolderSet.direct(holdergetter.getOrThrow(NullDimension.BIOME)),
                TerrainAdjustment.NONE)));
    }

    public static void bootstrapStructureSet(BootstapContext<StructureSet> context) {
        HolderGetter<Structure> holdergetter = context.lookup(Registries.STRUCTURE);
        context.register(Misc.ONE_TIME_STRUCTURE_SET,
                new StructureSet(holdergetter.getOrThrow(NULLZONE_HUB),
                        OneTimeStructurePlacement.INSTANCE));

        context.register(Misc.DESTABILIZER_SET,
                new StructureSet(holdergetter.getOrThrow(DESTABILIZER),
                        OneTimeStructurePlacement.INSTANCE));

    }

    private static Structure.StructureSettings structure(HolderSet<Biome> biomes, Map<MobCategory, StructureSpawnOverride> spawnOverrides, GenerationStep.Decoration step, TerrainAdjustment terrainAdaptation) {
        return new Structure.StructureSettings(biomes, spawnOverrides, step, terrainAdaptation);
    }

    private static Structure.StructureSettings structure(HolderSet<Biome> biomes, GenerationStep.Decoration step, TerrainAdjustment terrainAdaptation) {
        return structure(biomes, Map.of(), step, terrainAdaptation);
    }

    private static Structure.StructureSettings structure(HolderSet<Biome> biomes, TerrainAdjustment terrainAdaptation) {
        return structure(biomes, Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, terrainAdaptation);
    }
}
