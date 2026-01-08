package tfar.collapsecycle.platform;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.lang3.tuple.Pair;
import tfar.collapsecycle.platform.services.IPlatformHelper;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {

        return "Forge";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.isProduction();
    }

    public static final List<Pair<String,Supplier<? extends Item>>> ITEMS = new ArrayList<>();
    public static final List<Pair<String,Supplier<? extends Block>>> BLOCKS = new ArrayList<>();

    @Override
    public <I extends Block> Supplier<I> blockSupplier(String id, Supplier<I> supplier) {
        BLOCKS.add(Pair.of(id,supplier));
        return supplier;
    }

    @Override
    public <I extends Item> Supplier<I> itemSupplier(String id, Supplier<I> supplier) {
        ITEMS.add(Pair.of(id,supplier));
        return supplier;
    }

    @Override
    public Path getSaveDirectory() {
        return FMLPaths.GAMEDIR.get();
    }
}