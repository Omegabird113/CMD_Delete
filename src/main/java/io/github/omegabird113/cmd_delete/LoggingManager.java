package io.github.omegabird113.cmd_delete;

import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingManager {
    public static Logger getLogger(@NonNull Class<?> clazz) {
        return LoggerFactory.getLogger(CmdDeleteClient.MODID + "/" + clazz.getSimpleName());
    }

    static Logger getInitializerLogger() {
        return LoggerFactory.getLogger(CmdDeleteClient.MODID);
    }
}
