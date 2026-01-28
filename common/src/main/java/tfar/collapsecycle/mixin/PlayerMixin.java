package tfar.collapsecycle.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.collapsecycle.NullDimension;
import tfar.collapsecycle.PlayerDuck;

import java.util.Optional;

@Mixin(Player.class)
public class PlayerMixin implements PlayerDuck {

    @Unique
    int timeInBeam;

    @Override
    public int timeInBeam() {
        return timeInBeam;
    }

    @Override
    public void setTimeInBeam(int timeInBeam) {
        this.timeInBeam = timeInBeam;
    }

    @Inject(method = "findRespawnPositionAndUseSpawnBlock",at = @At("RETURN"),cancellable = true)
    private static void onRespawn(ServerLevel serverLevel, BlockPos spawnBlockPos, float playerOrientation, boolean isRespawnForced,
                                  boolean respawnAfterWinningTheGame, CallbackInfoReturnable<Optional<Vec3>> cir) {
        if (cir.getReturnValue().isEmpty()) {
            ResourceKey<Level> dimension = serverLevel.dimension();
            if (dimension == NullDimension.DIMENSION) {
                cir.setReturnValue(Optional.of(Vec3.atBottomCenterOf(spawnBlockPos)));
            }
        }
    }
}
