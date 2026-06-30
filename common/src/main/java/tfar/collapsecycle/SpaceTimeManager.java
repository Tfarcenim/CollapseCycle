package tfar.collapsecycle;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import org.apache.commons.io.FileUtils;
import tfar.collapsecycle.platform.Services;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpaceTimeManager {
    public static FileFilter filter = SpaceTimeManager::shouldDelete;
    static List<String> SKIP = new ArrayList<>();
    static {
        SKIP.addAll(List.of("playerdata", "advancements", "level.dat","nullzone","dimensions", CollapseCycle.MOD_ID));
    }

    static boolean shouldDelete(File file) {
        String s = file.getName();
        return !SKIP.contains(s);
    }

    private static final File currentSaveRootDirectory = Services.PLATFORM.getSaveDirectory().toFile();

    public static void reset(MinecraftServer server) {
        CollapseCycleConfig.Server.CURRENT_SEED.set(server.overworld().random.nextLong());
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