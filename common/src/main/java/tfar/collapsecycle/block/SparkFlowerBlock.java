package tfar.collapsecycle.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import tfar.collapsecycle.CollapseCycle;

public class SparkFlowerBlock extends FlowerBlock implements BonemealableBlock {

    public static final TagKey<Block> PLACEABLE_ON = TagKey.create(Registries.BLOCK, CollapseCycle.id("sparkflower_placeable_on"));
    public SparkFlowerBlock(MobEffect suspiciousStewEffect, int effectDuration, Properties properties) {
        super(suspiciousStewEffect, effectDuration, properties);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(PLACEABLE_ON);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader p_256234_, BlockPos p_57304_, BlockState p_57305_, boolean p_57306_) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level p_222573_, RandomSource p_222574_, BlockPos p_222575_, BlockState p_222576_) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel p_222568_, RandomSource p_222569_, BlockPos p_222570_, BlockState p_222571_) {
        popResource(p_222568_, p_222570_, new ItemStack(this));
    }
}
