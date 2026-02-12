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
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.structures.DesertPyramidStructure;
import net.minecraft.world.level.levelgen.structure.structures.JungleTempleStructure;
import tfar.collapsecycle.CollapseCycle;
import tfar.collapsecycle.NullDimension;
import tfar.collapsecycle.levelgen.OneTimeStructure;

import java.util.Map;

public class ModStructures {
    public static final ResourceKey<Structure> DESTABILIZER = createKey("destabilizer");
    public static final ResourceKey<Structure> NULLZONE_HUB = createKey("nullzone_hub");

    private static ResourceKey<Structure> createKey(String name) {
        return ResourceKey.create(Registries.STRUCTURE, CollapseCycle.id(name));
    }

    public static void bootstrap(BootstapContext<Structure> context) {
        HolderGetter<Biome> holdergetter = context.lookup(Registries.BIOME);
        context.register(DESTABILIZER, new OneTimeStructure(structure(holdergetter.getOrThrow(BiomeTags.IS_OVERWORLD),
                TerrainAdjustment.NONE)));
        context.register(NULLZONE_HUB, new OneTimeStructure(structure(HolderSet.direct(holdergetter.getOrThrow(NullDimension.BIOME)),
                TerrainAdjustment.NONE)));
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
