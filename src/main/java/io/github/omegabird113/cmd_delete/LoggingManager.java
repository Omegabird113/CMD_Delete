package io.github.omegabird113.cmd_delete;

import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LoggingManager {
    private LoggingManager() {
    }

    @Contract("_ -> new")
    public static @NonNull Logger getLogger(@NonNull Class<?> clazz) {
        return LoggerFactory.getLogger(CmdDeleteClient.MODID + "/" + clazz.getSimpleName());
    }
}
