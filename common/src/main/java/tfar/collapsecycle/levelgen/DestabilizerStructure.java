package tfar.collapsecycle.levelgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import tfar.collapsecycle.init.Misc;

import java.util.Optional;

public class DestabilizerStructure extends SinglePieceStructure {

    public static final Codec<DestabilizerStructure> CODEC = simpleCodec(DestabilizerStructure::new);


    public DestabilizerStructure(StructureSettings settings) {
        super(Piece::new, 12, 15, settings);
    }

    public static class Piece extends ScatteredFeaturePiece {

        public Piece(CompoundTag tag) {
            super(Misc.DESTABILIZER_PIECE, tag);
        }

        public Piece(RandomSource random, int x, int z) {
            super(Misc.DESTABILIZER_PIECE, x, 64, z, 12, 10, 15, getRandomHorizontalDirection(random));
        }


        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {

        }

        @Override
        public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pos) {

        }
    }

    @Override
    public StructureType<?> type() {
        return Misc.DESTABILIZER;
    }
}
