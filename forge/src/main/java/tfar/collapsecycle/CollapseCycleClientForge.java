package tfar.collapsecycle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraftforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class CollapseCycleClientForge {

    public static final IGuiOverlay OVERLAY = (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> CollapseCycleClient.renderPortalOverlay(guiGraphics,partialTick);

    public static void init(IEventBus bus) {
        MinecraftForge.EVENT_BUS.addListener(CollapseCycleClientForge::renderBeam);
        MinecraftForge.EVENT_BUS.addListener(CollapseCycleClientForge::viewport);
        bus.addListener(CollapseCycleClientForge::registerOverlay);
        bus.addListener(CollapseCycleClientForge::registerDimEffects);
        bus.addListener(CollapseCycleClientForge::setup);
    }

    static void setup(FMLClientSetupEvent event) {
        CollapseCycleClient.renderTypes(ItemBlockRenderTypes::setRenderLayer);
    }

    static void registerOverlay(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("overlay",OVERLAY);
    }

    static void renderBeam(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_BLOCK_ENTITIES) {
            CollapseCycleClient.renderBeam(event.getLevelRenderer(),event.getPoseStack(),event.getPartialTick(),false,event.getCamera());
        }
    }

    static void registerDimEffects(RegisterDimensionSpecialEffectsEvent event){
        event.register(NullDimension.ID,NullDimension.SPECIAL_EFFECTS);
    }

    static void viewport(ViewportEvent.ComputeCameraAngles event) {
        float pitch = event.getPitch();
        float roll = event.getRoll();
        float yaw = event.getYaw();
        CollapseCycleClient.setupCameraAngles(pitch,yaw,roll, event::setPitch, event::setYaw, event::setRoll);
    }
}
