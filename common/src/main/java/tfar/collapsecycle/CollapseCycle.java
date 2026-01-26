package tfar.collapsecycle;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import tfar.collapsecycle.init.ModBlocks;
import tfar.collapsecycle.init.ModItems;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

// This class is part of the common project meaning it is shared between all supported loaders. Code written here can only
// import and access the vanilla codebase, libraries used by vanilla, and optionally third party libraries that provide
// common compatible binaries. This means common code can not directly use loader specific concepts such as Forge events
// however it will be compatible with all supported mod loaders.
public class CollapseCycle {

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
    }

    public static void levelTick(ServerLevel level) {
        ResourceKey<Level> dimension = level.dimension();
        if (corruptible(dimension)) {
            long gameTime = level.getGameTime();
            long limit = CollapseCycleConfig.Server.TIME_LIMIT.get();
            if (gameTime >= limit) {
                if (gameTime == limit) {
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

    public static boolean corruptible(ResourceKey<Level> dimension) {
        return dimension == Level.OVERWORLD || dimension == Level.NETHER;
    }

    static final int DELAY = 50;

    public static void playerTick(Player player) {
        Level level = player.level();
        ResourceKey<Level> dimension = level.dimension();
        if (corruptible(dimension)) {
            long gameTime = level.getGameTime();
            long limit = CollapseCycleConfig.Server.TIME_LIMIT.get();
            if (gameTime >= limit) {
                Vec3 pos = player.position();
                PlayerDuck playerDuck = (PlayerDuck) player;
                if (Math.abs(pos.x) < 1 && Math.abs(pos.z) < 1) {
                    int timeinBeam = playerDuck.timeInBeam();
                    if (player instanceof ServerPlayer serverPlayer) {
                        ServerLevel serverLevel = serverPlayer.server.getLevel(NullDimension.DIMENSION);
                        if (serverLevel == null) {
                            return;
                        }
                        if (timeinBeam >= DELAY) {
                            serverPlayer.teleportTo(serverLevel, 0, 2, 0, 0, 0);
                            playerDuck.reset();
                        }
                        //player.server.execute(() -> player.changeDimension(serverLevel));
                    }
                    playerDuck.setTimeInBeam(timeinBeam+1);
                } else {
                    playerDuck.reset();
                }
            }
        }
    }

    static void beginCorruption(ServerLevel level) {

    }

    public static ResourceLocation id(String key) {
        return new ResourceLocation(Constants.MOD_ID, key);
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
}