package tfar.collapsecycle.platform;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import tfar.collapsecycle.CollapseCycle;
import tfar.collapsecycle.platform.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;
import java.util.function.Supplier;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public <I extends Block> Supplier<I> blockSupplier(String id, Supplier<I> supplier) {
        Registry.register(BuiltInRegistries.BLOCK, CollapseCycle.id(id),supplier.get());
        return supplier;
    }

    @Override
    public <I extends Item> Supplier<I> itemSupplier(String id, Supplier<I> supplier) {
        Registry.register(BuiltInRegistries.ITEM, CollapseCycle.id(id),supplier.get());
        return supplier;
    }

    @Override
    public Path getSaveDirectory() {
        return FabricLoader.getInstance().getGameDir();
    }
}
