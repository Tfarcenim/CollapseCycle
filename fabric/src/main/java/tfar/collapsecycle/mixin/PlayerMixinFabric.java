package tfar.collapsecycle.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.collapsecycle.CollapseCycle;

@Mixin(Player.class)
public class PlayerMixinFabric {
    @Inject(method = "tick",at = @At("TAIL"))
    private void onEndTick(CallbackInfo ci) {
        CollapseCycle.playerTick((Player)(Object)this);
    }
}
