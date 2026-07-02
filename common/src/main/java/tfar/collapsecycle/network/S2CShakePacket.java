package tfar.collapsecycle.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import tfar.collapsecycle.CollapseCycleClient;
import tfar.collapsecycle.platform.Services;

public enum S2CShakePacket implements S2CModPacket{
    INSTANCE;

    @Override
    public void handleClient() {
        CollapseCycleClient.shakeScreen();
    }

    public void send(ServerPlayer player) {
        Services.PLATFORM.sendToClient(this,player);
    }

    @Override
    public void write(FriendlyByteBuf to) {

    }
}
