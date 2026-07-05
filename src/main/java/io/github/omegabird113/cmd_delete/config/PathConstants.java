package io.github.omegabird113.cmd_delete.config;

import io.github.omegabird113.cmd_delete.LoggingManager;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

import java.nio.file.Path;

public final class PathConstants {
    private static @Nullable Path mappingsResourcePath;
    private static @Nullable Path activeMappingsFilePath;
    private static @Nullable Path mappingsJSONPath;
    private static boolean initialized = false;
    private static final Logger LOGGER = LoggingManager.getLogger(PathConstants.class);

    private PathConstants() {
    }

    public static void init(@NonNull Path gamePath, @NonNull Path mappingsResourcePath) {
        if (initialized)
            throw new IllegalStateException("PathConstants has already been initialized");

        PathConstants.mappingsResourcePath = mappingsResourcePath;
        PathConstants.activeMappingsFilePath = gamePath.resolve("config/cmd_delete/.active_mappings");
        PathConstants.mappingsJSONPath = gamePath.resolve("config/cmd_delete/mappings/");

        initialized = true;
        MappingsJSONManager.tryMakeConfigFiles();
        LOGGER.debug("Initialized paths locations for the mod... (mappingsResourcePath=\"{}\", mappingsJSONPath=\"{}\", activeMappingsFilePath=\"{}\", gamePath=\"{}\")", PathConstants.mappingsResourcePath, PathConstants.mappingsJSONPath, PathConstants.activeMappingsFilePath, gamePath);
    }

    public static @NonNull Path getMappingsResourcePath() {
        if (mappingsResourcePath == null)
            throw new IllegalStateException("Mappings resource path has not been set");
        return mappingsResourcePath;
    }

    public static @NonNull Path getActiveMappingsFilePath() {
        if (activeMappingsFilePath == null)
            throw new IllegalStateException("Active mappings file path has not been set");
        return activeMappingsFilePath;
    }

    public static @NotNull Path getMappingsJSONPath() {
        if (mappingsJSONPath == null)
            throw new IllegalStateException("Mappings JSON path has not been set");
        return mappingsJSONPath;
    }
}
