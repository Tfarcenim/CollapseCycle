package tfar.collapsecycle;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ProgressListener;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.apache.commons.io.FileUtils;
import tfar.collapsecycle.platform.Services;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class SpaceTimeManager {
    public static FileFilter filter = SpaceTimeManager::shouldDelete;
    static List<String> SKIP = new ArrayList<>();
    static {
        SKIP.addAll(List.of("playerdata", "advancements", "level.dat","nullzone","dimensions",Constants.MOD_ID));
    }

    static boolean shouldDelete(File file) {
        String s = file.getName();
        return !SKIP.contains(s);
    }

    private static final File currentSaveRootDirectory = Services.PLATFORM.getSaveDirectory().toFile();

    public static void reset(MinecraftServer server) {
        try {
            File file = server.storageSource.levelDirectory.path().toFile();
            delete(file);

           // ((MinecraftServerDuck)server).resetNether();
            ((MinecraftServerDuck)server).resetOverworld();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void resetWorld(ServerLevel level) {
            try {
            } catch (Exception var5) {
                var5.printStackTrace();
            }
    }

    static void delete(File worldDir) throws IOException {
            for (File f : worldDir.listFiles(filter)) {
                try {
                    if (f.isDirectory()) FileUtils.deleteDirectory(f);
                    else f.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }