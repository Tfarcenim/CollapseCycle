package tfar.collapsecycle.init;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import tfar.collapsecycle.CollapseCycle;
import tfar.collapsecycle.levelgen.OneTimeStructure;

import java.util.Locale;

public class Misc {
    public static final StructureType<OneTimeStructure> ONE_TIME_STRUCTURE = register("one_time_structure",
            OneTimeStructure.CODEC);

    public static final StructurePieceType ONE_TIME_STRUCTURE_PIECE = setPieceId(OneTimeStructure.Piece::new, "one_time_structure");

    private static <S extends Structure> StructureType<S> register(String name, Codec<S> codec) {
        return Registry.register(BuiltInRegistries.STRUCTURE_TYPE, CollapseCycle.id(name), () -> codec);
    }


    private static StructurePieceType setFullContextPieceId(StructurePieceType pieceType, String pieceId) {
        return Registry.register(BuiltInRegistries.STRUCTURE_PIECE, pieceId.toLowerCase(Locale.ROOT), pieceType);
    }

    private static StructurePieceType setPieceId(StructurePieceType.ContextlessType type, String key) {
        return setFullContextPieceId(type, key);
    }

    private static StructurePieceType setTemplatePieceId(StructurePieceType.StructureTemplateType templateType, String pieceId) {
        return setFullContextPieceId(templateType, pieceId);
    }

}
