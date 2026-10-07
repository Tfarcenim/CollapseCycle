package tfar.collapsecycle.levelgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import tfar.collapsecycle.init.Misc;
import tfar.collapsecycle.init.ModStructures;

public class DestabilizerStructure extends SinglePieceStructure {

    public static final Codec<DestabilizerStructure> CODEC = simpleCodec(DestabilizerStructure::new);


    public DestabilizerStructure(StructureSettings settings) {
        super(Piece::create, 15, 0, settings);
    }

    public static class Piece extends ScatteredFeaturePiece {

        public Piece(CompoundTag tag) {
            super(Misc.DESTABILIZER_PIECE, tag);
        }

        protected Piece(StructurePieceType type, int x, int y, int z, int width, int height, int depth, Direction orientation) {
            super(type, x, y, z, width, height, depth, orientation);
        }

        public static Piece create(RandomSource random, int chunkX, int chunkZ) {
            return new Piece(Misc.DESTABILIZER_PIECE, chunkX, 64, chunkZ, 15, 21, 15,
                    getRandomHorizontalDirection(random));
        }

        @Override
        public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pos) {
            StructureTemplateManager structuretemplatemanager = level.getLevel().getServer().getStructureManager();
            StructureTemplate structuretemplate = structuretemplatemanager.getOrCreate(ModStructures.DESTABILIZER.location());
            StructurePlaceSettings structureplacesettings = new StructurePlaceSettings().setRandom(random);
            BlockPos placePos = level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG,pos);
            boolean b = structuretemplate.placeInWorld(level, placePos.offset(-7,0,-7), placePos, structureplacesettings, random, 2);
        }
    }

    @Override
    public StructureType<?> type() {
        return Misc.DESTABILIZER;
    }
}
