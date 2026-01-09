package tfar.collapsecycle.mixin;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import tfar.collapsecycle.MinecraftServerDuck;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin implements MinecraftServerDuck {

    boolean resetting;

    @Override
    public void markResetting(boolean resetting) {
        this.resetting = resetting;
    }
}
