package tfar.collapsecycle.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.collapsecycle.CollapseCycleFabric;

@Mixin(BoneMealItem.class)
public class BonemealItemFabricMixin {
    @Inject(method = "growCrop",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getBlock()Lnet/minecraft/world/level/block/Block;",ordinal = 0))
    private static void onGrow(ItemStack stack, Level level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        int hook = CollapseCycleFabric.onApplyBonemeal(level, pos,level.getBlockState(pos), stack);
        if (hook!=0) cir.setReturnValue(hook >0);
    }
}
