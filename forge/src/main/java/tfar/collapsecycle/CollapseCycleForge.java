package tfar.collapsecycle;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.RegisterEvent;
import tfar.collapsecycle.datagen.ModDatagen;
import tfar.collapsecycle.platform.ForgePlatformHelper;

import java.util.function.Supplier;

@Mod(Constants.MOD_ID)
public class CollapseCycleForge {
    
    public CollapseCycleForge() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(ModDatagen::gather);
        bus.addListener(this::register);
        // This method is invoked by the Forge mod loader when it is ready
        // to load your mod. You can access Forge and Common code in this
        // project.
    
        // Use Forge to bootstrap the Common mod.
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER,CollapseCycleConfig.Server.SPEC);

        if (FMLEnvironment.dist.isClient()) {
            CollapseCycleClientForge.init(bus);
        }
        MinecraftForge.EVENT_BUS.addListener(this::fallingCorrupt);
        CollapseCycle.init();
    }

    void fallingCorrupt(TickEvent.LevelTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.side == LogicalSide.SERVER) {
            CollapseCycle.levelTick((ServerLevel) event.level);
        }
    }

    void register(RegisterEvent event) {
        if ((Registry<?>)event.getVanillaRegistry() == BuiltInRegistries.ITEM) {
            ForgePlatformHelper.ITEMS.forEach(stringSupplierPair -> event.register(Registries.ITEM,CollapseCycle.id(stringSupplierPair.getKey()),
                    (Supplier<Item>)stringSupplierPair.getValue()));
        } else if ((Registry<?>)event.getVanillaRegistry() == BuiltInRegistries.BLOCK) {
            ForgePlatformHelper.BLOCKS.forEach(stringSupplierPair -> event.register(Registries.BLOCK,CollapseCycle.id(stringSupplierPair.getKey()),
                    (Supplier<Block>)stringSupplierPair.getValue()));
        } else if ((Registry<?>)event.getVanillaRegistry() == BuiltInRegistries.CREATIVE_MODE_TAB) {
            ForgePlatformHelper.TABS.forEach(stringSupplierPair -> event.register(Registries.CREATIVE_MODE_TAB,CollapseCycle.id(stringSupplierPair.getKey()),
                    stringSupplierPair.getValue()));
        }
    }
}