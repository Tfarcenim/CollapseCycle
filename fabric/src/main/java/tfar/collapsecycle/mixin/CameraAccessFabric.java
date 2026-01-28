package tfar.collapsecycle.mixin;

import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Camera.class)
public interface CameraAccessFabric {
    @Accessor
    void setXRot(float xRot);

    @Accessor
    void setYRot(float yRot);
}
