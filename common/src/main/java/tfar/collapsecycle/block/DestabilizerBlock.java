package tfar.collapsecycle.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.storage.PrimaryLevelData;
import net.minecraft.world.level.storage.ServerLevelData;
import org.jetbrains.annotations.Nullable;
import tfar.collapsecycle.CollapseCycle;
import tfar.collapsecycle.CollapseCycleConfig;
import tfar.collapsecycle.CollapseSavedData;

public class DestabilizerBlock extends Block implements EntityBlock {
    public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;

    public DestabilizerBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(TRIGGERED,false));
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        MinecraftServer server = level.getServer();
        for (ServerLevel serverLevel : server.getAllLevels()) {
            if (CollapseCycle.corruptible(serverLevel.dimension()) && server.getGameRules().getBoolean(CollapseCycle.ACTIVE)) {
                CollapseSavedData.getOrMake(serverLevel).destabilize();
            }
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        boolean flag = level.hasNeighborSignal(pos) || level.hasNeighborSignal(pos.above());
        boolean flag1 = state.getValue(TRIGGERED);
        if (flag && !flag1) {
            level.scheduleTick(pos, this, 4);
            level.setBlock(pos, state.setValue(TRIGGERED, true), 4);
        } else if (!flag && flag1) {
            level.setBlock(pos, state.setValue(TRIGGERED,false), 4);
        }

    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TRIGGERED);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return state.getValue(TRIGGERED) ? new DestabilizerBlockEntity(pos, state) : null;
    }
}
//--Destabilizer: a block that when given a Redstone
//signal, spawns the beacon and triggers the collapse. (Not craftable or obtainable, only spawns in extract structures)