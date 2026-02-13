package tfar.collapsecycle.init;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import tfar.collapsecycle.CollapseCycle;
import tfar.collapsecycle.levelgen.DestabilizerStructure;
import tfar.collapsecycle.levelgen.OneTimeStructure;
import tfar.collapsecycle.levelgen.OneTimeStructurePlacement;

import java.util.Locale;

public class Misc {

    public static final String ID = "one_time_structure";

    public static final StructureType<OneTimeStructure> ONE_TIME_STRUCTURE = register(ID,
            OneTimeStructure.CODEC);

    public static final StructureType<DestabilizerStructure> DESTABILIZER = register("destabilizer",
            DestabilizerStructure.CODEC);

    public static final StructurePieceType ONE_TIME_STRUCTURE_PIECE = setPieceId(OneTimeStructure.Piece::new, ID);
    public static final StructurePieceType DESTABILIZER_PIECE = setPieceId(DestabilizerStructure.Piece::new,"destabilizer");

    public static final ResourceKey<StructureSet> ONE_TIME_STRUCTURE_SET = register(ID);
    public static final ResourceKey<StructureSet> DESTABILIZER_SET = register("destabilizer");
    public static final StructurePlacementType<OneTimeStructurePlacement> ONE_TIME_STRUCTURE_PLACEMENT = registerPlacement(ID,
            OneTimeStructurePlacement.CODEC);


    public static void init() {

    }

    private static <SP extends StructurePlacement> StructurePlacementType<SP> registerPlacement(String name, Codec<SP> codec) {
        return Registry.register(BuiltInRegistries.STRUCTURE_PLACEMENT, name, () -> codec);
    }

    private static ResourceKey<StructureSet> register(String name) {
        return ResourceKey.create(Registries.STRUCTURE_SET, CollapseCycle.id(name));
    }

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
