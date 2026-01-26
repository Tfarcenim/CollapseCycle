package tfar.collapsecycle.item;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import tfar.collapsecycle.init.ModBlocks;

public class GrasswandItem extends Item {
    public GrasswandItem(Properties properties) {
        super(properties);
    }


    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        if (state.is(ModBlocks.NULLSTONE.get())) {
            if (!level.isClientSide) {
                int damage = grassboi((ServerLevel) level,pos);
                ItemStack stack = context.getItemInHand();
                Player player = context.getPlayer();
                if (player != null) {
                    stack.hurtAndBreak(damage, player, (player1) -> player1.broadcastBreakEvent(context.getHand()));
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.FAIL;
    }

    int grassboi(ServerLevel level,BlockPos origin) {
        int r = 2;
        int g =0;
        for(int z = -r; z <= r;z++){
            for(int x = -r; x <= r;x++){
                BlockPos pos = origin.offset(x,0,z);
                if (level.getBlockState(pos).is(ModBlocks.NULLSTONE.get())) {
                    level.setBlockAndUpdate(pos,Blocks.GRASS_BLOCK.defaultBlockState());
                    g++;
                }
            }
        }
        return g;

    }

}
//--Grasswand: Turns surrounding Nullstone into normal grass blocks. (Crafted with a grass block, 2 blaze rods, and 2 amethyst shards.
// 50 uses, each use converts a 5x5 area into grass.)
