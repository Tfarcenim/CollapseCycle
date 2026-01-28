package tfar.collapsecycle;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class CollapseCycleClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register(CollapseCycleClient::renderPortalOverlay);
        DimensionRenderingRegistry.registerDimensionEffects(NullDimension.ID,NullDimension.SPECIAL_EFFECTS);
        CollapseCycleClient.renderTypes(BlockRenderLayerMap.INSTANCE::putBlock);
    }
}
