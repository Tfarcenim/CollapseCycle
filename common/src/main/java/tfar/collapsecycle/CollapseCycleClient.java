package tfar.collapsecycle;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import it.unimi.dsi.fastutil.floats.FloatConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import tfar.collapsecycle.init.ModBlocks;
import tfar.collapsecycle.network.S2CSetCollapseInfo;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CollapseCycleClient {

    public static final ResourceLocation BEAM = CollapseCycle.id("textures/entity/beam.png");
    static boolean active;
    static long countdown = 1;

    public static void renderBeam(LevelRenderer levelRenderer, PoseStack poseStack, float partialTick, boolean renderBlockOutline, Camera camera) {
        Level level = Minecraft.getInstance().level;
        if (CollapseCycle.corruptible(level.dimension())) {
            if (active && countdown<=0) {
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

    public static long getCountdown() {
        return countdown;
    }

    /**
     * Renders the portal overlay effect on the screen with the specified alpha value.
     *
     * @param guiGraphics the graphics object used for rendering.
     * @param alpha       the alpha value of the overlay.
     */
    public static void renderPortalOverlay(Gui gui,GuiGraphics guiGraphics, float alpha) {
        if (alpha < 1.0F) {
            alpha *= alpha;
            alpha *= alpha;
            alpha = alpha * 0.8F + 0.2F;
        }

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, alpha);
        TextureAtlasSprite textureatlassprite = Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getParticleIcon(Blocks.NETHER_PORTAL.defaultBlockState());
        guiGraphics.blit(0, 0, -90, guiGraphics.guiWidth(), guiGraphics.guiHeight(), textureatlassprite);
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static void renderPortalOverlay(GuiGraphics guiGraphics, float v) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            int ticksInBeam = ((PlayerDuck)player).timeInBeam();
            float alpha = (float) ticksInBeam/CollapseCycle.DELAY;
            if (alpha > 0) {
                renderPortalOverlay(Minecraft.getInstance().gui,guiGraphics,alpha);
            }
        }
    }

    public static void renderTypes(BiConsumer<Block, RenderType> consumer) {
        consumer.accept(ModBlocks.SPARKFLOWER.get(),RenderType.cutout());
        consumer.accept(ModBlocks.OVERWORLD_PORTAL.get(),RenderType.translucent());
    }

    public static void handle(S2CSetCollapseInfo s2CSetCollapseInfo) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null) {
            countdown = s2CSetCollapseInfo.countdown();
            active =s2CSetCollapseInfo.active();
        }
    }

    public static void setupCameraAngles(float pitch, float yaw, float roll, FloatConsumer pitchSetter, FloatConsumer yawSetter,
                                         FloatConsumer rollSetter) {
        double instability =  CollapseCycle.instability(Minecraft.getInstance().level);
        double adj = CollapseCycleConfig.Client.SHAKE_SCALAR.get().function.applyAsDouble(instability);
        double shakiness = CollapseCycleConfig.Client.SHAKE_AMOUNT.get() * adj;
        pitchSetter.accept(shake(pitch,shakiness));
        yawSetter.accept(shake(yaw,shakiness));
        rollSetter.accept(shake(roll,shakiness));
    }

    public static float shake(float angle,double shakiness) {
        return (float) (angle + (2 * Math.random()-1) * (shakiness * 360));
    }
}
