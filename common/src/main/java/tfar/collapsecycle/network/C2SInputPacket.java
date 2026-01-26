package tfar.collapsecycle.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class C2SInputPacket implements C2SModPacket{

    private final String name;

    public C2SInputPacket(FriendlyByteBuf buffer) {
        name= buffer.readUtf();
    }

    public C2SInputPacket(String name) {
        this.name = name;
    }


    @Override
    public void handleServer(ServerPlayer player) {

    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeUtf(name);
    }
}
