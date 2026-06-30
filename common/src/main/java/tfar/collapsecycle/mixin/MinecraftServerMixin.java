package tfar.collapsecycle.mixin;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.server.level.progress.ChunkProgressListenerFactory;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.ai.village.VillageSiege;
import net.minecraft.world.entity.npc.CatSpawner;
import net.minecraft.world.entity.npc.WanderingTraderSpawner;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.PatrolSpawner;
import net.minecraft.world.level.levelgen.PhantomSpawner;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.storage.*;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import tfar.collapsecycle.ducks.MinecraftServerDuck;
import tfar.collapsecycle.platform.Services;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.OptionalLong;
import java.util.concurrent.Executor;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin implements MinecraftServerDuck {

    @Shadow @Final private Map<ResourceKey<Level>, ServerLevel> levels;
    @Shadow @Final private Executor executor;
    @Shadow @Final public LevelStorageSource.LevelStorageAccess storageSource;

    @Shadow public abstract LayeredRegistryAccess<RegistryLayer> registries();

    @Shadow @Final private LayeredRegistryAccess<RegistryLayer> registries;
    @Shadow @Final private ChunkProgressListenerFactory progressListenerFactory;
    @Shadow @Final protected WorldData worldData;

    @Shadow public abstract ServerLevel overworld();

    @Shadow @Final private static Logger LOGGER;
    @Shadow protected long nextTickTime;

    @Shadow protected abstract void waitUntilNextTick();

    @Shadow protected abstract void updateMobSpawningFlags();

    @Shadow public abstract boolean isSpawningMonsters();

    @Shadow public abstract boolean isSpawningAnimals();

    @Override
    public void resetNether() {
        ServerLevel serverLevel = levels.get(Level.NETHER);

        ServerLevelData serverleveldata = this.worldData.overworldData();

        boolean flag = this.worldData.isDebugWorld();
        WorldOptions worldoptions = this.worldData.worldGenOptions();
        long i = worldoptions.seed();
        long j = BiomeManager.obfuscateSeed(i);
        List<CustomSpawner> list = List.of();
        ChunkProgressListener chunkprogresslistener = this.progressListenerFactory.create(11);
        Registry<LevelStem> registry = this.registries.compositeAccess().registryOrThrow(Registries.LEVEL_STEM);
        DerivedLevelData derivedleveldata = new DerivedLevelData(this.worldData, serverleveldata);


        LevelStem levelstem = registry.get(LevelStem.NETHER);

        ServerLevel newLevel = new ServerLevel((MinecraftServer) (Object)this, this.executor,

                this.storageSource, derivedleveldata, Level.NETHER, levelstem, chunkprogresslistener, flag, j, list, true, null);
        levels.put(Level.NETHER,newLevel);
        Services.PLATFORM.markWorldDirty((MinecraftServer) (Object) this);
    }

    /**
     * @see MinecraftServer#createLevels(ChunkProgressListener)
     *
     */
    @Override
    public void resetOverworld() {
        ServerLevel oldServerLevel = overworld();
        try {

    //        this.nextTickTime = Util.getMillis() + 1L;

           // for(ServerLevel serverlevel1 : this.getAllLevels()) {
                oldServerLevel.getChunkSource().removeTicketsOnClosing();
                oldServerLevel.getChunkSource().tick(() -> {
                    return true;
                }, false);
           // }

   //         this.waitUntilNextTick();

            oldServerLevel.close();
        } catch (IOException e) {
            //throw new RuntimeException(e);
        }

        ServerLevelData serverleveldata = this.worldData.overworldData();

        if (serverleveldata instanceof PrimaryLevelData primaryLevelData) {
            primaryLevelData.setDayTime(0);
            primaryLevelData.setGameTime(0);
            WorldOptions worldOptions = primaryLevelData.worldGenOptions().withSeed(OptionalLong.empty());
            ((PrimaryLevelDataAccess)primaryLevelData).setWorldOptions(worldOptions);
        }

        boolean flag = this.worldData.isDebugWorld();
        WorldOptions worldoptions = this.worldData.worldGenOptions();
        long i = worldoptions.seed();
        long j = BiomeManager.obfuscateSeed(i);
        List<CustomSpawner> list = ImmutableList.of(new PhantomSpawner(), new PatrolSpawner(), new CatSpawner(), new VillageSiege(), new WanderingTraderSpawner(serverleveldata));
        ChunkProgressListener chunkprogresslistener = this.progressListenerFactory.create(11);
        Registry<LevelStem> registry = this.registries.compositeAccess().registryOrThrow(Registries.LEVEL_STEM);


        LevelStem levelstem = registry.get(LevelStem.OVERWORLD);

        ServerLevel newLevel = new ServerLevel((MinecraftServer) (Object)this, this.executor,

                this.storageSource, serverleveldata, Level.OVERWORLD, levelstem, chunkprogresslistener, flag, j, list, true, null);
        levels.put(Level.OVERWORLD,newLevel);

        //////////////////////prepare

        LOGGER.info("Preparing start region for dimension {}", newLevel.dimension().location());
        BlockPos blockpos = newLevel.getSharedSpawnPos();
        chunkprogresslistener.updateSpawnPos(new ChunkPos(blockpos));
        ServerChunkCache serverchunkcache = newLevel.getChunkSource();
      //  this.nextTickTime = Util.getMillis();
        serverchunkcache.addRegionTicket(TicketType.START, new ChunkPos(blockpos), 11, Unit.INSTANCE);

    /*    while(serverchunkcache.getTickingGenerated() != 441) {
            this.nextTickTime = Util.getMillis() + 10L;
            this.waitUntilNextTick();
        }

        this.nextTickTime = Util.getMillis() + 10L;
        this.waitUntilNextTick();*/

        /*for(ServerLevel serverlevel1 : this.levels.values()) {
            ForcedChunksSavedData forcedchunkssaveddata = serverlevel1.getDataStorage().get(ForcedChunksSavedData::load, "chunks");
            if (forcedchunkssaveddata != null) {
                LongIterator longiterator = forcedchunkssaveddata.getChunks().iterator();

                while(longiterator.hasNext()) {
                    long i1 = longiterator.nextLong();
                    ChunkPos chunkpos = new ChunkPos(i1);
                    serverlevel1.getChunkSource().updateChunkForced(chunkpos, true);
                }
            }
        }*/

      //  this.nextTickTime = Util.getMillis() + 10L;
       // this.waitUntilNextTick();
        chunkprogresslistener.stop();
        newLevel.setSpawnSettings(this.isSpawningMonsters(), this.isSpawningAnimals());
        Services.PLATFORM.markWorldDirty((MinecraftServer) (Object) this);

    }
}
