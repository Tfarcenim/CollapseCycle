package tfar.collapsecycle.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import tfar.collapsecycle.CollapseCycle;
import tfar.collapsecycle.CollapseCycleConfig;
import tfar.collapsecycle.platform.Services;

import java.util.List;

public class CollapseClockItem extends Item {
    public CollapseClockItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        if (level != null) {
            tooltipComponents.add(Component.literal("Time remaining: " + CollapseCycle.getCountdown(level)));
            tooltipComponents.add(Component.literal("Active: " + CollapseCycle.isActive(level)));
            if (Services.PLATFORM.isDevelopmentEnvironment()) {
                tooltipComponents.add(Component.literal("Collapse Location: "+CollapseCycle.getCorruptionPos(level)));
            }
        }
    }
}
//--Collapse Clock: an item that displays how long until collapse in its tooltip. (Craftable with a clock, corrupt, observer, 2 iron, and 1 tinted-glass)
