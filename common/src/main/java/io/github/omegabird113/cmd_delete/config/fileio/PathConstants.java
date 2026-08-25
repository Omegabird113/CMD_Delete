/*
 * Copyright (c) 2026 Omegabird113.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.omegabird113.cmd_delete.config.fileio;

import io.github.omegabird113.cmd_delete.config.data.MappingsIdResolutionUtils;
import io.github.omegabird113.cmd_delete.mappings.MappingsType;
import io.github.omegabird113.cmd_delete.utils.LoggingManager;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

import java.nio.file.Path;

public final class PathConstants {
    private static final @NonNull Logger LOGGER = LoggingManager.getLoggerFor(PathConstants.class);

    private static @Nullable Path mappingsResourcePath;
    private static @Nullable Path activeMappingsFilePath;
    private static @Nullable Path mappingsJSONPath;
    private static boolean initialized = false;

    private PathConstants() {
    }

    public static void init(final @NonNull Path gamePath, final @NonNull Path mappingsResourcePath) {
        if (initialized)
            throw new IllegalStateException("PathConstants has already been initialized");

        PathConstants.mappingsResourcePath = mappingsResourcePath;
        PathConstants.activeMappingsFilePath = gamePath.resolve("config/cmd_delete/.active_mappings");
        PathConstants.mappingsJSONPath = gamePath.resolve("config/cmd_delete/mappings/");

        MappingsJSONManager.tryMakeConfigFiles();
        LoggingManager.debugLog(LOGGER, "Initialized paths locations for the mod... (mappingsResourcePath=\"{}\", mappingsJSONPath=\"{}\", activeMappingsFilePath=\"{}\", gamePath=\"{}\")", PathConstants.mappingsResourcePath, PathConstants.mappingsJSONPath, PathConstants.activeMappingsFilePath, gamePath);

        initialized = true;
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

    public static @NonNull Path getPathOf(final @NonNull MappingsType mappingsType, final @NonNull String id) {
        final Path path = (mappingsType == MappingsType.CUSTOM)
                ? getMappingsJSONPath()
                : getMappingsResourcePath();
        return path.resolve(id + ".json");
    }

    public static @NonNull Path getPathOf(final @NonNull String namespacedId) {
        return getPathOf(
                MappingsIdResolutionUtils.resolveType(namespacedId),
                MappingsIdResolutionUtils.removeNamespaceFromId(namespacedId)
        );
    }
}
