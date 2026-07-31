package io.github.omegabird113.cmd_delete.utils;

import io.github.omegabird113.cmd_delete.CmdDeleteClient;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LoggingManager {
    private LoggingManager() {
    }

    @Contract("_, _ -> new")
    public static @NonNull Logger getLogger(final @NotNull String modid, final @NonNull Class<?> clazz) {
        return LoggerFactory.getLogger(modid + "/" + clazz.getSimpleName());
    }

    @Contract("_ -> new")
    public static @NonNull Logger getLogger(final @NonNull Class<?> clazz) {
        return getLogger(CmdDeleteClient.MODID, clazz);
    }
}
