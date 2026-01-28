package tfar.collapsecycle.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import tfar.collapsecycle.CollapseCycle;

public class ModDamageSource {
    public static final ResourceKey<DamageType> COLLAPSE = ResourceKey.create(Registries.DAMAGE_TYPE, CollapseCycle.id("collapse"));

    public static void bootstrap(BootstapContext<DamageType> context) {
        context.register(COLLAPSE, new DamageType("collapse", 0F));
    }
}
