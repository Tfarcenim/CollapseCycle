package tfar.collapsecycle.levelgen;

import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Vec3i;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.structure.placement.*;
import tfar.collapsecycle.init.Misc;

import java.util.Optional;

public class ExcludeOriginStructurePlacement extends RandomSpreadStructurePlacement {

    public static final Codec<ExcludeOriginStructurePlacement> CODEC =
            RecordCodecBuilder.create(instance -> codec(instance)
                    .apply(instance, ExcludeOriginStructurePlacement::new));
    private final int excludeRadius;

    public int excludeRadius() {
        return excludeRadius;
    }

    public ExcludeOriginStructurePlacement(Vec3i locateOffset, StructurePlacement.FrequencyReductionMethod frequencyReductionMethod,
                                           float frequency, int salt, Optional<ExclusionZone> exclusionZone, int spacing,
                                           int separation, RandomSpreadType spreadType,int excludeRadius) {
      super(locateOffset, frequencyReductionMethod, frequency, salt, exclusionZone, spacing, separation, spreadType);
      this.excludeRadius = excludeRadius;
    }

    public ExcludeOriginStructurePlacement(int spacing, int separation, RandomSpreadType spreadType, int salt,int excludeRadius) {
        this(Vec3i.ZERO, StructurePlacement.FrequencyReductionMethod.DEFAULT, 1.0F, salt, Optional.empty(),
                spacing, separation, spreadType,excludeRadius);
    }

    @Override
    protected boolean isPlacementChunk(ChunkGeneratorStructureState structureState, int x, int z) {
        return super.isPlacementChunk(structureState, x, z) && Math.abs(x) >= this.excludeRadius && Math.abs(z) >= this.excludeRadius;
    }

    @Override
    public StructurePlacementType<?> type() {
        return Misc.EXCLUDE_ORIGIN_STRUCTURE_PLACEMENT;
    }

    private static Products.P9<RecordCodecBuilder.Mu<ExcludeOriginStructurePlacement>,
            Vec3i, StructurePlacement.FrequencyReductionMethod, Float, Integer, Optional<StructurePlacement.ExclusionZone>,
            Integer, Integer,RandomSpreadType, Integer>
    codec(RecordCodecBuilder.Instance<ExcludeOriginStructurePlacement> instance) {
        Products.P5<RecordCodecBuilder.Mu<ExcludeOriginStructurePlacement>, Vec3i, StructurePlacement.FrequencyReductionMethod, Float, Integer, Optional<StructurePlacement.ExclusionZone>> p5 = placementCodec(instance);
        Products.P4<RecordCodecBuilder.Mu<ExcludeOriginStructurePlacement>, Integer, Integer, RandomSpreadType, Integer> p4 =
                instance.group(
                        Codec.intRange(0, 4096).fieldOf("spacing").forGetter(RandomSpreadStructurePlacement::spacing),
                        Codec.intRange(0, 4096).fieldOf("separation").forGetter(RandomSpreadStructurePlacement::separation),
                        RandomSpreadType.CODEC.optionalFieldOf("spread_type", RandomSpreadType.LINEAR).forGetter(RandomSpreadStructurePlacement::spreadType),
                        ExtraCodecs.NON_NEGATIVE_INT.fieldOf("exclude_radius").forGetter(ExcludeOriginStructurePlacement::excludeRadius));
        return new Products.P9<>(p5.t1(), p5.t2(), p5.t3(), p5.t4(), p5.t5(), p4.t1(), p4.t2(), p4.t3(), p4.t4());
    }

}
