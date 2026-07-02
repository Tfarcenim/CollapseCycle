package tfar.collapsecycle.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.phys.Vec3;
import tfar.collapsecycle.block.DestabilizerBlockEntity;

public class DestabilizerBlockEntityRenderer implements BlockEntityRenderer<DestabilizerBlockEntity> {
    public DestabilizerBlockEntityRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(DestabilizerBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        float width = 2;
        BeaconRenderer.renderBeaconBeam(poseStack,buffer,BeaconRenderer.BEAM_LOCATION,partialTick,1,
                blockEntity.getLevel().getGameTime(),0,1024,new float[]{.5f, 0, 1},.2f * width, .25f * width);
    }

    @Override
    public boolean shouldRenderOffScreen(DestabilizerBlockEntity blockEntity) {
        return true;
    }

    @Override
    public int getViewDistance() {
        return 256;
    }

    @Override
    public boolean shouldRender(DestabilizerBlockEntity blockEntity, Vec3 cameraPos) {
        return Vec3.atCenterOf(blockEntity.getBlockPos()).multiply(1.0D, 0.0D, 1.0D).closerThan(cameraPos.multiply(1.0D, 0.0D, 1.0D), (double)this.getViewDistance());
    }
}
