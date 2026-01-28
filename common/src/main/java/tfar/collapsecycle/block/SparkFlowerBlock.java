package tfar.collapsecycle.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import tfar.collapsecycle.CollapseCycle;

public class SparkFlowerBlock extends FlowerBlock {

    public static final TagKey<Block> PLACEABLE_ON = TagKey.create(Registries.BLOCK, CollapseCycle.id("sparkflower_placeable_on"));
    public SparkFlowerBlock(MobEffect suspiciousStewEffect, int effectDuration, Properties properties) {
        super(suspiciousStewEffect, effectDuration, properties);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(PLACEABLE_ON);
    }
}
