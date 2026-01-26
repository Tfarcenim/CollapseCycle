package tfar.collapsecycle;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class CollapseCycleClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register(CollapseCycleClient::renderBeamOverlay);
    }
}
