package tfar.collapsecycle;

import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.saveddata.SavedData;
import tfar.collapsecycle.init.ModSounds;
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
            if (countdown == 0) {
                playGlobalSound(true);
            }
        }
        CollapseCycle.sendToPlayersInLevel(level,new S2CSetCollapseInfo(active(), countdown));
    }

    void playGlobalSound(boolean natural) {
        SoundEvent soundEvent = natural ? ModSounds.COLLAPSE_START : ModSounds.UNNATURAL_COLLAPSE_START;
        for (ServerPlayer player : level.getServer().getPlayerList().getPlayers()) {
        player.connection.send(new ClientboundSoundPacket(
                Holder.direct(soundEvent), SoundSource.AMBIENT,player.getX(),player.getY(),player.getZ(),
                1,1,0));
        }

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

    public static CollapseSavedData getOrMakeDefault(MinecraftServer server){
        ServerLevel overworld = server.overworld();
        return getOrMake(overworld);
    }

    public void destabilize() {
        if (countdown > 0) {
            countdown = 0;
            playGlobalSound(false);
        }
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
