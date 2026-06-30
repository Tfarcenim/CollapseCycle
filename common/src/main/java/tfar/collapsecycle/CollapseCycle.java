package tfar.collapsecycle;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tfar.collapsecycle.ducks.PlayerDuck;
import tfar.collapsecycle.init.Misc;
import tfar.collapsecycle.init.ModBlocks;
import tfar.collapsecycle.init.ModDamageSource;
import tfar.collapsecycle.init.ModItems;
import tfar.collapsecycle.network.S2CModPacket;
import tfar.collapsecycle.network.S2CSetCollapseInfo;
import tfar.collapsecycle.platform.Services;

import java.util.List;

// This class is part of the common project meaning it is shared between all supported loaders. Code written here can only
// import and access the vanilla codebase, libraries used by vanilla, and optionally third party libraries that provide
// common compatible binaries. This means common code can not directly use loader specific concepts such as Forge events
// however it will be compatible with all supported mod loaders.
public class CollapseCycle {

    public static final String MOD_ID = "collapsecycle";
    public static final String MOD_NAME = "CollapseCycle";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    // The loader specific projects are able to import and use any code from the common project. This allows you to
    // write the majority of your code here and load it from your loader specific projects. This example has some
    // code that gets invoked by the entry point of the loader specific projects.
    public static void init() {


        // It is common for all supported loaders to provide a similar feature that can not be used directly in the
        // common code. A popular way to get around this is using Java's built-in service loader feature to create
        // your own abstraction layer. You can learn more about this in our provided services class. In this example
        // we have an interface in the common code and use a loader specific implementation to delegate our call to
        // the platform specific approach.
        ModBlocks.init();
        ModItems.init();
        Misc.init();
    }

    public static final GameRules.Key<GameRules.BooleanValue> ACTIVE = GameRules.register("collapsecycle:collapse_active",
            GameRules.Category.PLAYER, GameRules.BooleanValue.create(true));

    public static void levelTick(ServerLevel level) {
        ResourceKey<Level> dimension = level.dimension();

        if (corruptible(dimension)) {
            CollapseSavedData data = CollapseSavedData.getOrMake(level);
            data.tick();
            long countdown = data.countdown();
            if (countdown <= 0) {
                if (countdown == 0) {
                    beginCorruption(level);
                } else {
                    Iterable<ChunkHolder> chunks = level.getChunkSource().chunkMap.getChunks();
                    for (ChunkHolder chunkHolder : chunks) {
                        LevelChunk fullChunk = chunkHolder.getTickingChunk();
                        if (fullChunk != null) {
                            if (level.random.nextDouble() < 1 / 1024f) {
                                ChunkPos pos = fullChunk.getPos();
                                int x = level.random.nextInt(16);
                                int z = level.random.nextInt(16);
                                fullChunk.setBlockState(new BlockPos(x, 256, z), ModBlocks.CORRUPT.get().defaultBlockState(), false);
                                level.scheduleTick(pos.getBlockAt(x, 256, z), ModBlocks.CORRUPT.get(), 1);
                            }
                        }
                    }
                }
            }
        }
    }

    public static final BlockPos NULLZONE_SPAWN = new BlockPos(0,3,0);

    public static void onDeath(LivingEntity livingEntity, DamageSource source) {
        if (livingEntity instanceof ServerPlayer player && source.is(ModDamageSource.COLLAPSE)) {
            player.setRespawnPosition(NullDimension.DIMENSION,NULLZONE_SPAWN,0,true,false);
            Services.PLATFORM.sendToClient(new S2CSetCollapseInfo(false,CollapseCycleConfig.Server.TIME_LIMIT.get()),player);
        }
    }

    public static <MSG extends S2CModPacket> void sendToPlayersInLevel(ServerLevel level,MSG msg) {
        for (ServerPlayer player : level.players()) {
            Services.PLATFORM.sendToClient(msg, player);
        }
    }

    public static boolean corruptible(ResourceKey<Level> dimension) {
        return dimension == Level.OVERWORLD || dimension == Level.NETHER;
    }

    static final int DELAY = 50;

    public static void playerTick(Player player) {
        Level level = player.level();
        ResourceKey<Level> dimension = level.dimension();
        if (corruptible(dimension)) {
            long countdown = getCountdown(player.level());
            if (countdown <= 0) {
                int corruptionPos = getCorruptionPos(level);
                Vec3 pos = player.position();
                double x = Math.abs(pos.x);
                double z = Math.abs(pos.z);

                if (!level.isClientSide &&(x > corruptionPos || z > corruptionPos)) {
                    player.hurt(level.damageSources().source(ModDamageSource.COLLAPSE),1_000_000_000_000f);
                }


                PlayerDuck playerDuck = (PlayerDuck) player;
                if (x < 2 && z < 2) {
                    int timeinBeam = playerDuck.timeInBeam();
                    if (player instanceof ServerPlayer serverPlayer) {
                        ServerLevel serverLevel = serverPlayer.server.getLevel(NullDimension.DIMENSION);
                        if (serverLevel == null) {
                            return;
                        }
                        if (timeinBeam >= DELAY) {
                            serverPlayer.teleportTo(serverLevel, NULLZONE_SPAWN.getX()+.5, NULLZONE_SPAWN.getY(),
                                    NULLZONE_SPAWN.getZ()+.5, 0, 0);
                            Services.PLATFORM.sendToClient(new S2CSetCollapseInfo(false,CollapseCycleConfig.Server.TIME_LIMIT.get()),serverPlayer);
                            playerDuck.reset();
                        }
                    }
                    playerDuck.setTimeInBeam(timeinBeam + 1);
                } else {
                    playerDuck.reset();
                }
            }
        }
    }

    public static long getCountdown(Level level) {
        return level.isClientSide ? CollapseCycleClient.getCountdown() : CollapseSavedData.getOrMake((ServerLevel) level).countdown();
    }

    public static int getCorruptionPos(Level level) {
        int worldborderPos = level.getWorldBorder().getAbsoluteMaxSize();
        return (int) (worldborderPos * ( 1 - instability(level)));
    }

    public static double instability(Level level) {
        if (!corruptible(level.dimension())) return 0;
        long collapseEnd = CollapseCycleConfig.Server.TIME_TO_0_0.get();
        long countdown = getCountdown(level);
        if (countdown>=0) {
            return 0;
        }else if (countdown > -collapseEnd) {
            return -(double) countdown / collapseEnd;
        }
        return 1;
    }

    static void beginCorruption(ServerLevel level) {

    }

    public static boolean growCrops(Level level) {
        if (level.dimension() == NullDimension.DIMENSION) {
            return level.random.nextDouble() < .25;
        }
        return true;
    }

    public static boolean canUseBonemeal(Level level) {
        return level.dimension() != NullDimension.DIMENSION;
    }

    public static ResourceLocation id(String key) {
        return new ResourceLocation(MOD_ID, key);
    }

    public static void onDimChange(ServerPlayer player, ResourceKey<Level> dimension) {
        MinecraftServer server = player.server;
        if (dimension == NullDimension.DIMENSION) {
            List<ServerPlayer> remainingPlayers = server.overworld().players();
            if (remainingPlayers.isEmpty()) {
                server.execute(() -> SpaceTimeManager.reset(server));
            }
        }
    }

    public static boolean isActive(Level level) {
        return level.isClientSide ? CollapseCycleClient.active : CollapseSavedData.getOrMake((ServerLevel) level).active();
    }
}