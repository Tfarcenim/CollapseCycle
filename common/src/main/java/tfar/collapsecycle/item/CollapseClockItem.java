package tfar.collapsecycle.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import tfar.collapsecycle.CollapseCycleConfig;

import java.util.List;

public class CollapseClockItem extends Item {
    public CollapseClockItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        if (level != null) {
            tooltipComponents.add(Component.literal("Time remaining: " + (CollapseCycleConfig.Server.TIME_LIMIT.get() - level.getGameTime())));
        }
    }
}
//--Collapse Clock: an item that displays how long until collapse in its tooltip. (Craftable with a clock, corrupt, observer, 2 iron, and 1 tinted-glass)
