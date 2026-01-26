package tfar.collapsecycle;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import tfar.collapsecycle.network.S2CSetCollapseInfo;

public class CollapseSavedData extends SavedData {

    private final ServerLevel level;
    private long countdown;

    public CollapseSavedData(ServerLevel level) {
        this.level = level;
        countdown = CollapseCycleConfig.Server.TIME_LIMIT.get();
    }

    public static CollapseSavedData loadStatic(ServerLevel level, CompoundTag tag) {
        CollapseSavedData data = new CollapseSavedData(level);
        data.countdown= tag.getLong("countdown");
        return data;
    }

    public void tick() {
        if (active()) {
            countdown--;
        }
        CollapseCycle.sendToPlayersInLevel(level,new S2CSetCollapseInfo(active(), countdown));
    }

    public boolean active() {
        return level.getGameRules().getBoolean(CollapseCycle.ACTIVE) && !level.players().isEmpty();
    }

    @Override
    public boolean isDirty() {
        return active();
    }

    public static CollapseSavedData getOrMake(ServerLevel level){
        return level.getDataStorage().computeIfAbsent(tag -> loadStatic(level, tag), () -> new CollapseSavedData(level),
                level.dimension().location().toString().replace(":","."));
    }

    public void destabilize() {
        countdown = 0;
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag) {
        compoundTag.putLong("countdown",countdown);
        return compoundTag;
    }

    public long countdown() {
        return countdown;
    }
}
