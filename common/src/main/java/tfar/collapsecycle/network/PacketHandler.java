package tfar.collapsecycle.network;


import net.minecraft.resources.ResourceLocation;
import tfar.collapsecycle.CollapseCycle;
import tfar.collapsecycle.platform.Services;

import java.util.Locale;

public class PacketHandler {

    public static void registerPackets() {
        Services.PLATFORM.registerServerPacket(C2SInputPacket.class, C2SInputPacket::new);

        Services.PLATFORM.registerClientPacket(S2CSetCollapseInfo.class, S2CSetCollapseInfo::fromPacket);
        Services.PLATFORM.registerClientPacket(S2CShakePacket.class, f -> S2CShakePacket.INSTANCE);
    }

    public static ResourceLocation packet(Class<?> clazz) {
        return CollapseCycle.id(clazz.getName().toLowerCase(Locale.ROOT));
    }

}
