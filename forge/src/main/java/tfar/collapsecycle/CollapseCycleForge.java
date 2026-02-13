package tfar.collapsecycle;

import net.minecraft.ChatFormatting;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.RegisterEvent;
import tfar.collapsecycle.datagen.ModDatagen;
import tfar.collapsecycle.network.PacketHandler;
import tfar.collapsecycle.platform.ForgePlatformHelper;

import java.util.function.Supplier;

@Mod(Constants.MOD_ID)
public class CollapseCycleForge {
    
    public CollapseCycleForge() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(ModDatagen::gather);
        bus.addListener(this::register);
        bus.addListener(this::setup);
        // This method is invoked by the Forge mod loader when it is ready
        // to load your mod. You can access Forge and Common code in this
        // project.
    
        // Use Forge to bootstrap the Common mod.
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER,CollapseCycleConfig.Server.SPEC);

        if (FMLEnvironment.dist.isClient()) {
            CollapseCycleClientForge.init(bus);
        }
        MinecraftForge.EVENT_BUS.addListener(this::playerTick);
        MinecraftForge.EVENT_BUS.addListener(this::fallingCorrupt);
        MinecraftForge.EVENT_BUS.addListener(this::dimChange);
        MinecraftForge.EVENT_BUS.addListener(this::commands);
        MinecraftForge.EVENT_BUS.addListener(this::onDeath);
        MinecraftForge.EVENT_BUS.addListener(this::onCropsGrow);
        MinecraftForge.EVENT_BUS.addListener(this::onUseBonemeal);
        ((MappedRegistry<?>)BuiltInRegistries.STRUCTURE_PIECE).unfreeze();
        ((MappedRegistry<?>)BuiltInRegistries.STRUCTURE_TYPE).unfreeze();
        ((MappedRegistry<?>)BuiltInRegistries.STRUCTURE_PLACEMENT).unfreeze();
        CollapseCycle.init();
    }

    void onCropsGrow(BlockEvent.CropGrowEvent.Pre event) {
        Level level = (Level) event.getLevel();
        if (!CollapseCycle.growCrops(level)) {
            event.setResult(Event.Result.DENY);
        }
    }

    void onUseBonemeal(BonemealEvent event) {
        Level level = event.getLevel();
        if (!CollapseCycle.canUseBonemeal(level)) {
            if (level.isClientSide)
                event.getEntity().sendSystemMessage(Component.literal("Bonemeal doesn't work in this realm").withStyle(ChatFormatting.RED));
            event.setCanceled(true);
        }
    }

    void onDeath(LivingDeathEvent event){
        CollapseCycle.onDeath(event.getEntity(),event.getSource());
    }

    void dimChange(PlayerEvent.PlayerChangedDimensionEvent event) {
        CollapseCycle.onDimChange((ServerPlayer) event.getEntity(),event.getTo());
    }

    void playerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            CollapseCycle.playerTick(event.player);
        }
    }

    void commands(RegisterCommandsEvent event) {
        CollapseCycleCommands.register(event.getDispatcher());
    }

    void setup(FMLCommonSetupEvent event) {
        PacketHandler.registerPackets();
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