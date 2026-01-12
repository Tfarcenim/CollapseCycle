package tfar.collapsecycle;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class CollapseCycleClient {

    public static final ResourceLocation BEAM = CollapseCycle.id("textures/entity/beam.png");

    public static void renderBeam(LevelRenderer levelRenderer, PoseStack poseStack, float partialTick, boolean renderBlockOutline, Camera camera) {
        Level level = Minecraft.getInstance().level;
        if (CollapseCycle.corruptible(level.dimension())) {
            long limit = CollapseCycleConfig.Server.TIME_LIMIT.get();
            long gameTime = level.getGameTime();
            if (gameTime >= limit) {
                Vec3 vec3 = camera.getPosition();
                double d0 = vec3.x();
                double d1 = vec3.y();
                double d2 = vec3.z();
                MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
                Vec3 blockpos3 = Vec3.ZERO;

                int maxDist = 192;

                if (vec3.distanceToSqr(blockpos3) > maxDist * maxDist) {
                    blockpos3 = vec3.subtract(vec3.normalize().scale(maxDist));
                    blockpos3 = new Vec3(blockpos3.x, -64, blockpos3.z);
                }

                poseStack.pushPose();
                poseStack.translate(blockpos3.x - d0, blockpos3.y - d1, blockpos3.z - d2);


                long i = level.getGameTime();
                float width = 10;
                BeaconRenderer.renderBeaconBeam(poseStack, multibuffersource$buffersource,
                        BEAM, partialTick, 1, i, 0, 1024, new float[]{1, 1f, 0}, .2f * width, .25f * width);
                poseStack.popPose();
            }
        }
    }
}
