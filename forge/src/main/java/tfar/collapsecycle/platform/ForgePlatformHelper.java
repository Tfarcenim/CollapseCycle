package tfar.collapsecycle.platform;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.lang3.tuple.Pair;
import tfar.collapsecycle.ForgePacketHandler;
import tfar.collapsecycle.network.C2SModPacket;
import tfar.collapsecycle.network.S2CModPacket;
import tfar.collapsecycle.platform.services.IPlatformHelper;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
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
    public static final List<Pair<String,Supplier<CreativeModeTab>>> TABS = new ArrayList<>();

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
    public Supplier<CreativeModeTab> tabSupplier(String id, Supplier<CreativeModeTab> supplier) {
        TABS.add(Pair.of(id,supplier));
        return supplier;
    }

    @Override
    public Path getSaveDirectory() {
        return FMLPaths.GAMEDIR.get();
    }

    static int i;

    public <MSG extends C2SModPacket> void registerServerPacket(Class<MSG> packetLocation, Function<FriendlyByteBuf,MSG> reader) {
        ForgePacketHandler.INSTANCE.registerMessage(i++, packetLocation, MSG::write, reader, ForgePacketHandler.wrapC2S());
    }

    public <MSG extends S2CModPacket> void registerClientPacket(Class<MSG> packetLocation, Function<FriendlyByteBuf,MSG> reader) {
        ForgePacketHandler.INSTANCE.registerMessage(i++, packetLocation, MSG::write, reader, ForgePacketHandler.wrapS2C());
    }

    @Override
    public void sendToClient(S2CModPacket msg, ServerPlayer player) {
        ForgePacketHandler.sendToClient(msg,player);
    }

    @Override
    public void sendToServer(C2SModPacket msg) {
        ForgePacketHandler.sendToServer(msg);
    }

    @Override
    public void markWorldDirty(MinecraftServer server) {
        server.markWorldsDirty();
    }
}