package tfar.collapsecycle.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import tfar.collapsecycle.NullDimension;

import java.util.Set;

public class OverworldPortalBlock extends Block {

    protected static final VoxelShape SHAPE = Block.box(0.0D, 6.0D, 0.0D, 16.0D, 12.0D, 16.0D);

    public OverworldPortalBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }


    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level instanceof ServerLevel && entity.canChangeDimensions() && Shapes.joinIsNotEmpty(
                Shapes.create(entity.getBoundingBox().move(-pos.getX(), -pos.getY(), -pos.getZ())), state.getShape(level, pos), BooleanOp.AND)) {
            ResourceKey<Level> resourcekey = level.dimension() == NullDimension.DIMENSION ? Level.OVERWORLD : NullDimension.DIMENSION;
            ServerLevel serverlevel = ((ServerLevel)level).getServer().getLevel(resourcekey);
            if (serverlevel == null) {
                return;
            }

            int y = serverlevel.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,0,0);
            entity.teleportTo(serverlevel, 0, y, 0, Set.of(), entity.getYRot(), entity.getXRot());
        }
    }

    /**
     * Called periodically clientside on blocks near the player to show effects (like furnace fire particles).
     */
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        double d0 = (double)pos.getX() + random.nextDouble();
        double d1 = (double)pos.getY() + 0.8D;
        double d2 = (double)pos.getZ() + random.nextDouble();
        level.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canBeReplaced(BlockState state, Fluid fluid) {
        return false;
    }

}
