package tfar.collapsecycle.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.collapsecycle.CollapseCycleClient;

@Mixin(GameRenderer.class)
@Debug(export = true)
public class GameRendererMixinFabric {
    @Shadow @Final private Camera mainCamera;

    @Inject(method = "renderLevel",at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;setup(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/world/entity/Entity;ZZF)V",shift = At.Shift.AFTER))
    private void modifyAngles(float partialTicks, long finishTimeNano, PoseStack poseStack, CallbackInfo ci) {
        Camera camera = mainCamera;
        CameraAccessFabric cameraAccessFabric = (CameraAccessFabric) camera;

        float pitch = camera.getXRot();
        float yaw = camera.getYRot();
        final float[] roll = {0};
        CollapseCycleClient.setupCameraAngles(pitch,yaw, roll[0], cameraAccessFabric::setXRot, cameraAccessFabric::setYRot,
                f -> roll[0] = f);
        poseStack.mulPose(Axis.ZP.rotationDegrees(roll[0]));
    }
}
