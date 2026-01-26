package tfar.collapsecycle.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.collapsecycle.CollapseCycle;

@Mixin(ServerPlayer.class)
@Debug(export = true)
public class ServerPlayerMixinFabric {

    @Inject(method = "changeDimension",at = @At(value = "RETURN"))
    private void onDimChange(ServerLevel destination, CallbackInfoReturnable<Entity> cir) {
        CollapseCycle.onDimChange((ServerPlayer)(Object)this,destination.dimension());
    }


    @Inject(method = "teleportTo(Lnet/minecraft/server/level/ServerLevel;DDDFF)V",at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;sendAllPlayerInfo(Lnet/minecraft/server/level/ServerPlayer;)V"))
    private void onTeleport(ServerLevel newLevel, double x, double y, double z, float yaw, float pitch, CallbackInfo ci) {
        CollapseCycle.onDimChange((ServerPlayer) (Object)this,newLevel.dimension());
    }
}
