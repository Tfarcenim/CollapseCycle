package tfar.collapsecycle;

import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraftforge.fml.config.ModConfig;

public class CollapseCycleFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        
        // This method is invoked by the Fabric mod loader when it is ready
        // to load your mod. You can access Fabric and Common code in this
        // project.

        // Use Fabric to bootstrap the Common mod.
        ForgeConfigRegistry.INSTANCE.register(Constants.MOD_ID, ModConfig.Type.SERVER, CollapseCycleConfig.Server.SPEC);
        ServerTickEvents.START_WORLD_TICK.register(CollapseCycle::levelTick);

        CollapseCycle.init();
    }
}
