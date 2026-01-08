package tfar.collapsecycle.init;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import tfar.collapsecycle.item.CollapseClockItem;
import tfar.collapsecycle.platform.Services;

import java.util.function.Supplier;

public class ModItems {
    public static final Supplier<CollapseClockItem> COLLAPSE_CLOCK = Services.PLATFORM.itemSupplier("collapse_clock",
            () -> new CollapseClockItem(new Item.Properties()));

    public static final Supplier<BlockItem> NULLSTONE = Services.PLATFORM.itemSupplier("nullstone",() -> new BlockItem(ModBlocks.NULLSTONE.get(),
            new Item.Properties()));
}
//-Items:
//--Collapse Clock: an item that displays how long until collapse in its tooltip. (Craftable with a clock, corrupt, observer, 2 iron, and 1 tinted-glass)
//--Grasswand: Turns surrounding Nullstone into normal grass blocks. (Crafted with a grass block, 2 blaze rods, and 2 amethyst shards. 50 uses, each use converts a 5x5 area into grass.)
//-Blocks:
//--Nullstone: a white block that makes up the Nullzone. (Essentially reskinned stone)
//--corrupt: a block that appears during collapse, sometimes falling from the sky.
//--Destabilizer: a block that when given a Redstone
//signal, spawns the beacon and triggers the collapse. (Not craftable or obtainable, only spawns in extract structures)
//--sparkflower: a flower with a white stem and black flower. Emits a warm light. (Torch level of light, spawns semi-sparse. Can be bonemealed to reproduce.)