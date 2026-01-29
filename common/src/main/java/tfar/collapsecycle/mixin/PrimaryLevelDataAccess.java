package tfar.collapsecycle.mixin;

import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.storage.PrimaryLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PrimaryLevelData.class)
public interface PrimaryLevelDataAccess {
    @Accessor @Mutable void setWorldOptions(WorldOptions worldOptions);
}
