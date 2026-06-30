package tfar.collapsecycle;

import net.minecraft.server.MinecraftServer;
import org.apache.commons.io.FileUtils;
import tfar.collapsecycle.ducks.MinecraftServerDuck;
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
        SKIP.addAll(List.of("playerdata", "advancements", "level.dat","nullzone","dimensions","DIM1", CollapseCycle.MOD_ID));
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

            ((MinecraftServerDuck)server).resetOverworld();
            ((MinecraftServerDuck)server).resetNether();

        } catch (Exception e) {
            throw new RuntimeException(e);
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