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

package io.github.omegabird113.cmd_delete.mappings;

import io.github.omegabird113.cmd_delete.command.MappingsInfoCollectionUtils;
import io.github.omegabird113.cmd_delete.config.data.FeatureFlags;
import io.github.omegabird113.cmd_delete.config.data.MappingsIdResolutionUtils;
import io.github.omegabird113.cmd_delete.config.data.MappingsRegistry;
import io.github.omegabird113.cmd_delete.config.fileio.ActiveMappingsManager;
import io.github.omegabird113.cmd_delete.utils.LoggingManager;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;
import java.util.Optional;

public final class NavMappingsManager {
    private static final @NonNull Logger LOGGER = LoggingManager.getLoggerFor(NavMappingsManager.class);
    private static volatile @Nullable MappingsState currentMappingsState;

    private NavMappingsManager() {
    }

    public static @NonNull MappingsState getMappingsState() {
        final MappingsState current = currentMappingsState;
        if (current == null)
            throw new IllegalStateException("No current mappings state has been set, but the mappings were accessed");
        return current;
    }

    public static @NonNull Optional<MappingsState> getOptionalMappingsState() {
        return Optional.ofNullable(currentMappingsState);
    }

    public static @NonNull NavMappings getCurrentMappings() {
        return getMappingsState().mappings();
    }

    public static @NonNull MappingsRegistry getCurrentMappingsRegistry() {
        return getMappingsState().mappings().registry();
    }

    public static @NonNull FeatureFlags getCurrentFeatureFlags() {
        return getMappingsState().mappings().registry().featureFlags();
    }

    private static void logMappings() {
        LOGGER.info("Mappings id \"{}\" loaded with supported systems \"{}\" and Coverage of {}% with a registry size of {}. It supports the actions: {}", MappingsIdResolutionUtils.resolveNamespacedId(getMappingsState()), List.of(getCurrentMappings().getMappingsSupportedSystems()), getCurrentMappings().getCoverage() * 100, getCurrentMappings().registry().getSize(), getCurrentMappings().getPossibleActions());
        LOGGER.info("The active mappings' info in \"/navmappings info\" will show as: \"{}\"", MappingsInfoCollectionUtils.getInfoFrom(getMappingsState(), false).replace("\n", " "));
        LoggingManager.traceLog(LOGGER, "Mappings state loaded: \"{}\"", currentMappingsState);
    }

    public static void loadMappings() {
        MappingsState toLoad = ActiveMappingsManager.tryGetMappings();
        if (toLoad == null)
            toLoad = ActiveMappingsManager.resolveMappingsWithDefaultFallback("");
        currentMappingsState = toLoad;
        ActiveMappingsManager.trySaveMappings(
                MappingsIdResolutionUtils.resolveNamespacedId(toLoad)
        );
        logMappings();
    }

    public static boolean updateMappingsTo(final @NonNull MappingsType type, final @NonNull String id) {
        final MappingsState newState = ActiveMappingsManager.resolveMappings(
                MappingsIdResolutionUtils.resolveNamespacedId(type, id)
        );
        if (newState == null)
            return false;
        currentMappingsState = newState;
        ActiveMappingsManager.trySaveMappings(
                MappingsIdResolutionUtils.resolveNamespacedId(newState)
        );
        logMappings();
        return true;
    }
}
