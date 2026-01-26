package tfar.collapsecycle.mixin;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import tfar.collapsecycle.PlayerDuck;

@Mixin(Player.class)
public class PlayerMixin implements PlayerDuck {

    @Unique
    int timeInBeam;

    @Override
    public int timeInBeam() {
        return timeInBeam;
    }

    @Override
    public void setTimeInBeam(int timeInBeam) {
        this.timeInBeam = timeInBeam;
    }
}
