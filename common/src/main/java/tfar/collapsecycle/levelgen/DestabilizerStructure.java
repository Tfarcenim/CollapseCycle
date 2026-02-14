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
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import tfar.collapsecycle.init.Misc;
import tfar.collapsecycle.init.ModStructures;

public class DestabilizerStructure extends SinglePieceStructure {

    public static final Codec<DestabilizerStructure> CODEC = simpleCodec(DestabilizerStructure::new);


    public DestabilizerStructure(StructureSettings settings) {
        super(Piece::new, 15, 0, settings);
    }

    public static class Piece extends ScatteredFeaturePiece {

        public Piece(CompoundTag tag) {
            super(Misc.DESTABILIZER_PIECE, tag);
        }

        public Piece(RandomSource random, int x, int z) {
            super(Misc.DESTABILIZER_PIECE, x, 64, z, 15, 21, 15, getRandomHorizontalDirection(random));
        }

        @Override
        public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pos) {
            StructureTemplateManager structuretemplatemanager = level.getLevel().getServer().getStructureManager();
            StructureTemplate structuretemplate = structuretemplatemanager.getOrCreate(ModStructures.DESTABILIZER.location());
            StructurePlaceSettings structureplacesettings = new StructurePlaceSettings().setRandom(random);
            BlockPos placePos = level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG,pos);
            boolean b = structuretemplate.placeInWorld(level, placePos, placePos, structureplacesettings, random, 2);
        }
    }

    @Override
    public StructureType<?> type() {
        return Misc.DESTABILIZER;
    }
}
