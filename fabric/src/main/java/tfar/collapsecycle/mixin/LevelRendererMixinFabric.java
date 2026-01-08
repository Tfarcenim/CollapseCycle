package tfar.collapsecycle.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.collapsecycle.CollapseCycleClient;

@Mixin(LevelRenderer.class)
public class LevelRendererMixinFabric {

    @Inject(method = "renderLevel",at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V",ordinal = 12))
    private void onRender(PoseStack poseStack, float partialTick, long finishNanoTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer,
                          LightTexture lightTexture, Matrix4f projectionMatrix, CallbackInfo ci) {
        CollapseCycleClient.renderBeam((LevelRenderer) (Object)this,poseStack,partialTick,renderBlockOutline,camera);
    }
}
