package tfar.collapsecycle;

import net.minecraft.server.MinecraftServer;
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
        SKIP.addAll(List.of("playerdata", "advancements", "level.dat","nullzone"));
    }

    static boolean shouldDelete(File file) {
        String s = file.getName();
        if (SKIP.contains(s)) {
            return false;
        }
        return true;
    }

    private static File currentSaveRootDirectory = Services.PLATFORM.getSaveDirectory().toFile();

    public static void reset(MinecraftServer server) {
        try {
            server.storageSource.getLevelPath()
            delete(currentSaveRootDirectory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static void delete(File worldDir) throws IOException {
            for (File f : worldDir.listFiles(filter)) {
                if (f.isDirectory()) FileUtils.deleteDirectory(f);
                else f.delete();
            }
        }
    }