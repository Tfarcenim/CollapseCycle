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
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import tfar.collapsecycle.init.Misc;
import tfar.collapsecycle.init.ModStructures;

import java.util.Optional;

public class OneTimeStructure extends Structure {

    public static final Codec<OneTimeStructure> CODEC = simpleCodec(OneTimeStructure::new);

    public OneTimeStructure(StructureSettings settings) {
        super(settings);
    }


    public Optional<GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
        ChunkPos chunkPos = context.chunkPos();
        return Optional.of(new Structure.GenerationStub(new BlockPos(chunkPos.getMinBlockX(),2,chunkPos.getMinBlockZ()), builder ->
                        this.generatePieces(builder, context)));
    }

    private void generatePieces(StructurePiecesBuilder builder, Structure.GenerationContext context) {
        ChunkPos chunkpos = context.chunkPos();
        builder.addPiece(new Piece(chunkpos.getMinBlockX(), chunkpos.getMinBlockZ()));
    }


    public static class Piece extends StructurePiece{

        public Piece(CompoundTag tag) {
            super(Misc.ONE_TIME_STRUCTURE_PIECE, tag);
        }

        public Piece(int x, int z) {
            super(Misc.DESTABILIZER_PIECE,0, StructurePiece.makeBoundingBox(x, 2, z, Direction.NORTH,
                    22, 8, 19));
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {

        }

        @Override
        public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pos) {
            StructureTemplateManager structuretemplatemanager = level.getLevel().getServer().getStructureManager();
            StructureTemplate structuretemplate = structuretemplatemanager.getOrCreate(ModStructures.NULLZONE_HUB.location());
            StructurePlaceSettings structureplacesettings = new StructurePlaceSettings().setRandom(random);
            boolean b = structuretemplate.placeInWorld(level, pos.offset(-7,0, -2), pos, structureplacesettings, random, 2);
        }//22x8x19
    }

    @Override
    public StructureType<?> type() {
        return Misc.ONE_TIME_STRUCTURE;
    }
}
