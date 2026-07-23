package io.github.omegabird113.cmd_delete;

import io.github.omegabird113.cmd_delete.mappings.NavMappingsManager;
import net.minecraft.CrashReport;
import net.minecraft.client.Minecraft;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.function.Supplier;

public final class CrashUtils {
    private static final @NonNull Logger LOGGER = LoggingManager.getLogger(CrashUtils.class);

    private CrashUtils() {
    }

    public static <T> @Nullable T crashMinecraftOnFailure(@NonNull Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            crashMinecraft(e);
            return null;
        }
    }

    public static void crashMinecraftOnFailure(@NonNull Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            crashMinecraft(e);
        }
    }

    public static void crashMinecraft(@NonNull Throwable e) {
        LOGGER.error("A fatal error occurred and CMD + Delete must initiate a game crash...\nThe mappings state is:\n{}\nand the exception that occurred is:\n\t{}",
                NavMappingsManager.getOptionalMappingsState().orElse(null),
                String.join("\n\t", Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toArray(String[]::new))
        );
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.emergencySaveAndCrash(CrashReport.forThrowable(e, "CMD + Delete encountered an irrecoverable exception. Please report this at: https://github.com/Omegabird113/CMD_Delete/issues"));
    }
}
