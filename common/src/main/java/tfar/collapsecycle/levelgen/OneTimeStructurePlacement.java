package tfar.collapsecycle.levelgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import tfar.collapsecycle.init.Misc;

import java.util.Optional;

public class OneTimeStructurePlacement extends StructurePlacement {

    public static final OneTimeStructurePlacement INSTANCE = new OneTimeStructurePlacement();
    public static final Codec<OneTimeStructurePlacement> CODEC = Codec.unit(INSTANCE);
    protected OneTimeStructurePlacement() {
        super(Vec3i.ZERO, FrequencyReductionMethod.DEFAULT, 1, 0, Optional.empty());
    }

    @Override
    protected boolean isPlacementChunk(ChunkGeneratorStructureState structureState, int x, int z) {
        return x == 0 && z == 0;
    }

    @Override
    public StructurePlacementType<?> type() {
        return Misc.ONE_TIME_STRUCTURE_PLACEMENT;
    }
}
