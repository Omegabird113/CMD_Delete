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

    public static final boolean VERBOSE_LOGGING_ALLOWED = Boolean.getBoolean("cmd_delete.allowVerboseLogs");
    private static final Logger LOGGER = getLoggerFor(LoggingManager.class);

    static {
        if (VERBOSE_LOGGING_ALLOWED)
            debugLog(LOGGER, "LoggingManager initialized. Verbose logging is enabled with the \"cmd_delete.allowVerboseLogs\" JVM argument... This means messages from TRACE and DEBUG levels will be bumped to INFO for users to be able to reasonably obtain these detailed messages in their latest.log file for bug reports without messing with Log4j configurations and/or downloading 3rd-party launchers.");
        else
            debugLog(LOGGER, "LoggingManager initialized. Verbose logging is disabled. If you're debugging, consider setting the \"cmd_delete.allowVerboseLogs\" JVM argument to true...");
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

    public static void verboseLog(Logger logger, String format, Object... args) {
        if (VERBOSE_LOGGING_ALLOWED) {
            logger.info("[VERBOSE/TRACE]: " + format, args);
        } else {
            logger.trace(format, args);
        }
    }

    public static void debugLog(Logger logger, String format, Object... args) {
        if (VERBOSE_LOGGING_ALLOWED) {
            logger.info("[VERBOSE/DEBUG]: " + format, args);
        } else {
            logger.debug(format, args);
        }
    }
}
