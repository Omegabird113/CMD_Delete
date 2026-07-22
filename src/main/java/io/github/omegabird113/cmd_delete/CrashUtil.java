package io.github.omegabird113.cmd_delete;

import net.minecraft.CrashReport;
import net.minecraft.client.Minecraft;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;

import java.util.function.Supplier;

public final class CrashUtil {
    private static final @NonNull Logger LOGGER = LoggingManager.getLogger(CrashUtil.class);

    private CrashUtil() {
    }

    public static <T> T crashMinecraftOnFailure(Supplier<T> supplier) {
        T toReturn = null;
        try {
            toReturn = supplier.get();
        } catch (RuntimeException e) {
            LOGGER.error("A fatal error occurred and CMD + Delete must initiate a game crash...", e);
            Minecraft minecraft = Minecraft.getInstance();
            minecraft.emergencySaveAndCrash(CrashReport.forThrowable(e, "CMD + Delete encountered a irrecoverable exception. Please report this at: https://github.com/Omegabird113/CMD_Delete/issues"));
        }
        return toReturn;
    }

    public static void crashMinecraftOnFailure(Runnable runnable) {
        try {
            runnable.run();
        } catch (RuntimeException e) {
            LOGGER.error("A fatal error occurred and CMD + Delete must initiate a game crash...", e);
            Minecraft minecraft = Minecraft.getInstance();
            minecraft.emergencySaveAndCrash(CrashReport.forThrowable(e, "CMD + Delete encountered a irrecoverable exception. Please report this at: https://github.com/Omegabird113/CMD_Delete/issues"));
        }
    }
}
