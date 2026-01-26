package tfar.collapsecycle;

import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;

public class CollapseCycleClientForge {
    public static void init(IEventBus bus) {
        MinecraftForge.EVENT_BUS.addListener(CollapseCycleClientForge::renderBeam);
        MinecraftForge.EVENT_BUS.addListener(CollapseCycleClientForge::viewport);
    }

    static void renderBeam(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_BLOCK_ENTITIES) {
            CollapseCycleClient.renderBeam(event.getLevelRenderer(),event.getPoseStack(),event.getPartialTick(),false,event.getCamera());
        }
    }

    static void viewport(ViewportEvent.ComputeCameraAngles event) {

    }
}
