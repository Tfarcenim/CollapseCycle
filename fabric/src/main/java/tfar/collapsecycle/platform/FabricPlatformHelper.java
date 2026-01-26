package tfar.collapsecycle.platform;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.MixinEnvironment;
import tfar.collapsecycle.ClientPacketHandlerFabric;
import tfar.collapsecycle.CollapseCycle;
import tfar.collapsecycle.PacketHandlerFabric;
import tfar.collapsecycle.network.C2SModPacket;
import tfar.collapsecycle.network.PacketHandler;
import tfar.collapsecycle.network.S2CModPacket;
import tfar.collapsecycle.platform.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;
import java.util.function.Function;
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
    public  Supplier<CreativeModeTab> tabSupplier(String id, Supplier<CreativeModeTab> supplier) {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,CollapseCycle.id(id),supplier.get());
        return supplier;
    }

    @Override
    public Path getSaveDirectory() {
        return FabricLoader.getInstance().getGameDir();
    }

    @Override
    public <MSG extends S2CModPacket> void registerClientPacket(Class<MSG> packetLocation, Function<FriendlyByteBuf, MSG> reader) {
        if (MixinEnvironment.getCurrentEnvironment().getSide() == MixinEnvironment.Side.CLIENT) {
            ClientPacketHandlerFabric.register(packetLocation,reader);
        }
    }

    @Override
    public <MSG extends C2SModPacket> void registerServerPacket(Class<MSG> packetLocation, Function<FriendlyByteBuf, MSG> reader) {
        ServerPlayNetworking.registerGlobalReceiver(PacketHandler.packet(packetLocation), PacketHandlerFabric.wrapC2S(reader));
    }


    @Override
    public void sendToClient(S2CModPacket msg, ServerPlayer player) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        msg.write(buf);
        ServerPlayNetworking.send(player, PacketHandler.packet(msg.getClass()), buf);
    }

    @Override
    public void sendToServer(C2SModPacket msg) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        msg.write(buf);
        ClientPlayNetworking.send(PacketHandler.packet(msg.getClass()), buf);
    }

    @Override
    public void markWorldDirty(MinecraftServer server) {

    }


}
