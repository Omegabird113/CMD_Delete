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
    public static @NonNull Logger getLoggerFor(final @NotNull String modid, final @NonNull Class<?> clazz) {
        return LoggerFactory.getLogger(modid + "/" + clazz.getSimpleName());
    }

    @Contract("_ -> new")
    public static @NonNull Logger getLoggerFor(final @NonNull Class<?> clazz) {
        return getLoggerFor(CmdDeleteClient.MODID, clazz);
    }

    @Contract("_, _ -> new")
    public static @NonNull Logger getLoggerFor(final @NotNull String modid, final @NonNull Object o) {
        return LoggerFactory.getLogger(modid + "/" + o.getClass().getSimpleName() + "@" + Integer.toHexString(System.identityHashCode(o)));
    }

    @Contract("_ -> new")
    public static @NonNull Logger getLoggerFor(final @NonNull Object o) {
        return getLoggerFor(CmdDeleteClient.MODID, o);
    }
}
