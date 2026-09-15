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
        ChunkGenerator chunkGenerator = context.chunkGenerator();
        int y = chunkGenerator.getFirstFreeHeight(0, 0, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());
        BlockPos blockpos = new BlockPos(chunkPos.getMinBlockX()+7, y, chunkPos.getMinBlockZ()+7);
           return Optional.of(new Structure.GenerationStub(blockpos, builder ->
                           this.generatePiece(builder, blockpos)));
    }

    private void generatePiece(StructurePiecesBuilder builder, BlockPos pos) {
        builder.addPiece(new Piece(pos));
    }


    public static class Piece extends StructurePiece{

        public Piece(CompoundTag tag) {
            super(Misc.ONE_TIME_STRUCTURE_PIECE, tag);
        }

        public Piece(BlockPos pos) {
            super(Misc.DESTABILIZER_PIECE,0, new
                    BoundingBox(pos.getX(), pos.getY(), pos.getZ(),pos.getX()+21,pos.getY()+8,pos.getZ()+18));
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {

        }

        @Override
        public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pos) {
            StructureTemplateManager structuretemplatemanager = level.getLevel().getServer().getStructureManager();
            StructureTemplate structuretemplate = structuretemplatemanager.getOrCreate(ModStructures.NULLZONE_HUB.location());
            StructurePlaceSettings structureplacesettings = new StructurePlaceSettings().setRandom(random);
            boolean b = structuretemplate.placeInWorld(level, pos.offset(-11,0,-9), pos, structureplacesettings, random, 2);
        }//22x8x19
    }

    @Override
    public StructureType<?> type() {
        return Misc.ONE_TIME_STRUCTURE;
    }
}
