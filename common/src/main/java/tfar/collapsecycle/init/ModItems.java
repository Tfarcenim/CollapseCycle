package tfar.collapsecycle.init;

import com.google.common.base.Suppliers;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import tfar.collapsecycle.item.CollapseClockItem;
import tfar.collapsecycle.item.GrasswandItem;
import tfar.collapsecycle.platform.Services;

import java.util.function.Supplier;

public class ModItems {



    public static final Supplier<CollapseClockItem> COLLAPSE_CLOCK = Services.PLATFORM.itemSupplier("collapse_clock",
            Suppliers.memoize(() -> new CollapseClockItem(new Item.Properties())));

    public static final Supplier<GrasswandItem> GRASSWAND = Services.PLATFORM.itemSupplier("grasswand",
            Suppliers.memoize(() -> new GrasswandItem(new Item.Properties().durability(1250))));

    public static final Supplier<BlockItem> NULLSTONE = Services.PLATFORM.itemSupplier("nullstone",Suppliers.memoize(() -> new BlockItem(ModBlocks.NULLSTONE.get(),
            new Item.Properties())));

    public static final Supplier<BlockItem> DESTABILIZER = Services.PLATFORM.itemSupplier("destabilizer",Suppliers.memoize(() -> new BlockItem(ModBlocks.DESTABILIZER.get(),
            new Item.Properties())));

    public static final Supplier<BlockItem> CORRUPT = Services.PLATFORM.itemSupplier("corrupt",Suppliers.memoize(() -> new BlockItem(ModBlocks.CORRUPT.get(),
            new Item.Properties())));

    public static final Supplier<BlockItem> SPARKFLOWER = Services.PLATFORM.itemSupplier("sparkflower",Suppliers.memoize(() -> new BlockItem(ModBlocks.SPARKFLOWER.get(),
            new Item.Properties())));


    public static final Supplier<CreativeModeTab> TAB = Services.PLATFORM.tabSupplier("items",
            () -> CreativeModeTab.builder(null,-1).title(Component.translatable("itemGroup.collapsecycle"))
                    .icon(() -> COLLAPSE_CLOCK.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(COLLAPSE_CLOCK.get());
                        output.accept(DESTABILIZER.get());
                        output.accept(GRASSWAND.get());
                        output.accept(NULLSTONE.get());
                        output.accept(SPARKFLOWER.get());
                    })
                    .build());

    public static void init() {

    }
}
//-Items:
//-Blocks:
//--Nullstone: a white block that makes up the Nullzone. (Essentially reskinned stone)
//--corrupt: a block that appears during collapse, sometimes falling from the sky.

//--sparkflower: a flower with a white stem and black flower. Emits a warm light. (Torch level of light, spawns semi-sparse. Can be bonemealed to reproduce.)