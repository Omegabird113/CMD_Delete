package io.github.omegabird113.cmd_delete.utils;

import io.github.omegabird113.cmd_delete.CmdDeleteClient;
import io.github.omegabird113.cmd_delete.mappings.NavMappingsManager;
import net.minecraft.CrashReport;
import net.minecraft.client.Minecraft;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

import java.util.function.Supplier;

public final class CrashUtils {
    public static final boolean CRASHING_ALLOWED = !Boolean.getBoolean("cmd_delete.forcePreventMinecraftCrashes");
    private static final @NonNull Logger LOGGER = LoggingManager.getLoggerFor(CrashUtils.class);

    private CrashUtils() {
    }

    public static void sendLoadInfo() {
        if (!CRASHING_ALLOWED)
            LOGGER.warn("CMD + Delete is not allowed to crash Minecraft in this environment if it enters an invalid/irrecoverable state due to a JVM property (\"cmd_delete.forcePreventMinecraftCrashes\"). This means you may experience undefined behavior or the mod not working in the event that something goes terribly wrong.");
        else
            LoggingManager.debugLog(LOGGER, "Crashing is allowed in this environment. This can be prevented with the \"cmd_delete.forcePreventMinecraftCrashes\" JVM property if needed, though setting that property is not recommended.");
    }

    public static <T> @Nullable T crashMinecraftOnFailure(final @NonNull Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            tryCrashMinecraft(e);
            return null;
        }
    }

    public static void crashMinecraftOnFailure(final @NonNull Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            tryCrashMinecraft(e);
        }
    }

    public static void tryCrashMinecraft(final @NonNull Throwable e) {
        if (CRASHING_ALLOWED) {
            LOGGER.error("A fatal error occurred and CMD + Delete must initiate a game crash...\nThe mappings state is:\n{}\nand the exception that occurred is:",
                    NavMappingsManager.getOptionalMappingsState().orElse(null),
                    e
            );
            Minecraft.getInstance().emergencySaveAndCrash(CrashReport.forThrowable(e, "CMD + Delete encountered an irrecoverable exception. Please report this at: " + CmdDeleteClient.ISSUE_TRACKER_URL_STRING));
        } else
            LOGGER.error("A fatal error occurred and CMD + Delete was prevented from crashing...");
    }
}
