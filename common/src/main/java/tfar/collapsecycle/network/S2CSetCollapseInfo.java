package tfar.collapsecycle.network;

import net.minecraft.network.FriendlyByteBuf;
import tfar.collapsecycle.CollapseCycleClient;

public record S2CSetCollapseInfo(boolean active, long countdown) implements S2CModPacket{

    public static S2CSetCollapseInfo fromPacket(FriendlyByteBuf buf) {
        return new S2CSetCollapseInfo(buf.readBoolean(),buf.readLong());
    }

    @Override
    public void handleClient() {
        CollapseCycleClient.handle(this);
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeBoolean(active);
        to.writeLong(countdown);
    }
}
