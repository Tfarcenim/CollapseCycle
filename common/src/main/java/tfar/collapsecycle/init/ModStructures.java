package tfar.collapsecycle.init;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import tfar.collapsecycle.CollapseCycle;

public class ModStructures {
    public static final ResourceKey<Structure> DESTABILIZER_HUB = createKey("destabilizer_hub");

    private static ResourceKey<Structure> createKey(String name) {
        return ResourceKey.create(Registries.STRUCTURE, CollapseCycle.id(name));
    }

    public static void bootstrap(BootstapContext<Structure> context) {
        HolderGetter<Biome> holdergetter = context.lookup(Registries.BIOME);
        HolderGetter<StructureTemplatePool> holdergetter1 = context.lookup(Registries.TEMPLATE_POOL);
    }
}
