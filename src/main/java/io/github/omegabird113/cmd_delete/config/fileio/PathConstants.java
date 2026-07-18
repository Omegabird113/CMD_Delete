package io.github.omegabird113.cmd_delete.config.fileio;

import io.github.omegabird113.cmd_delete.LoggingManager;
import io.github.omegabird113.cmd_delete.config.data.MappingsIdResolutionUtils;
import io.github.omegabird113.cmd_delete.mappings.MappingsType;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

import java.nio.file.Path;

public final class PathConstants {
    private static final @NonNull Logger LOGGER = LoggingManager.getLogger(PathConstants.class);

    private static @Nullable Path mappingsResourcePath;
    private static @Nullable Path activeMappingsFilePath;
    private static @Nullable Path mappingsJSONPath;
    private static boolean initialized = false;

    private PathConstants() {
    }

    public static void init(@NonNull Path gamePath, @NonNull Path mappingsResourcePath) {
        if (initialized)
            throw new IllegalStateException("PathConstants has already been initialized");
        initialized = true;

        PathConstants.mappingsResourcePath = mappingsResourcePath;
        PathConstants.activeMappingsFilePath = gamePath.resolve("config/cmd_delete/.active_mappings");
        PathConstants.mappingsJSONPath = gamePath.resolve("config/cmd_delete/mappings/");

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

    public static @NonNull Path getMappingsJSONPath() {
        if (mappingsJSONPath == null)
            throw new IllegalStateException("Mappings JSON path has not been set");
        return mappingsJSONPath;
    }

    public static Path getPathOf(MappingsType mappingsType, String id) {
        final Path path = (mappingsType == MappingsType.CUSTOM)
                ? getMappingsJSONPath()
                : getMappingsResourcePath();
        return path.resolve(id + ".json");
    }

    public static Path getPathOf(String namespacedId) {
        return getPathOf(
                MappingsIdResolutionUtils.resolveType(namespacedId),
                MappingsIdResolutionUtils.removeNamespaceFromId(namespacedId)
        );
    }
}
