package tfar.collapsecycle.levelgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.structures.JungleTemplePiece;
import tfar.collapsecycle.init.Misc;

import java.util.Optional;

public class OneTimeStructure extends SinglePieceStructure {

    public static final Codec<OneTimeStructure> CODEC = simpleCodec(OneTimeStructure::new);

    public OneTimeStructure(StructureSettings settings) {
        super(JungleTemplePiece::new, 12, 15, settings);
    }

    public static class Piece extends StructurePiece{

        public Piece(CompoundTag tag) {
            super(Misc.ONE_TIME_STRUCTURE_PIECE, tag);
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {

        }

        @Override
        public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pos) {

        }
    }

    @Override
    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
        return onTopOfChunkCenter(context, Heightmap.Types.WORLD_SURFACE_WG, (structurePiecesBuilder) -> {

        });
    }

    @Override
    public StructureType<?> type() {
        return Misc.ONE_TIME_STRUCTURE;
    }
}
