package tfar.collapsecycle;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

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

                }
            }
        }
    }

    public static boolean corruptible(ResourceKey<Level> dimension) {
        return dimension == Level.OVERWORLD || dimension ==  Level.NETHER;
    }

    public static void playerTick(ServerPlayer player) {
        ServerLevel level = player.serverLevel();
        ResourceKey<Level> dimension = level.dimension();
        if (corruptible(dimension)) {
            long gameTime = level.getGameTime();
            long limit = CollapseCycleConfig.Server.TIME_LIMIT.get();
            if (gameTime >= limit) {
                Vec3 pos = player.position();
                if (Math.abs(pos.x) < 1 && Math.abs(pos.z)<1) {
                    ServerLevel serverLevel = player.server.getLevel(NullDimension.DIMENSION);
                    if (serverLevel == null) {
                        return;
                    }
                    player.teleportTo(serverLevel,0,2,0,0,0);
                    //player.server.execute(() -> player.changeDimension(serverLevel));
                }
            }
        }
    }

    static void beginCorruption(ServerLevel level) {

    }

    public static ResourceLocation id(String key) {
        return new ResourceLocation(Constants.MOD_ID,key);
    }

    public static void onDimChange(ServerPlayer player, ResourceKey<Level> dimension) {
        MinecraftServer server = player.server;
        if (dimension == NullDimension.DIMENSION) {
            List<ServerPlayer> remainingPlayers = server.overworld().players();
            if (remainingPlayers.isEmpty()) {
                SpaceTimeManager.reset(player.server);
            }
        }
    }
}