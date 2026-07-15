package io.github.omegabird113.cmd_delete.mappings;

import io.github.omegabird113.cmd_delete.LoggingManager;
import io.github.omegabird113.cmd_delete.command.MappingsInfoCollectionUtils;
import io.github.omegabird113.cmd_delete.config.data.FeatureFlags;
import io.github.omegabird113.cmd_delete.config.data.MappingsIdResolutionUtils;
import io.github.omegabird113.cmd_delete.config.data.MappingsRegistry;
import io.github.omegabird113.cmd_delete.config.fileio.ActiveMappingsManager;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;

public final class NavMappingsManager {
    private static final @NonNull Logger LOGGER = LoggingManager.getLogger(NavMappingsManager.class);
    private static final @NonNull ActiveMappingsManager ACTIVE_MAPPINGS_MANAGER = new ActiveMappingsManager();
    private static @Nullable MappingsState currentMappingsState;

    private NavMappingsManager() {
    }

    public static @NonNull MappingsState getMappingsState() {
        if (currentMappingsState == null)
            throw new IllegalStateException("No current mappings state has been set, but the mappings were accessed");
        return currentMappingsState;
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
        LOGGER.debug("Mappings state loaded: \"{}\"", currentMappingsState);
    }

    public static void loadMappings() {
        currentMappingsState = ACTIVE_MAPPINGS_MANAGER.tryGetMappings();
        ACTIVE_MAPPINGS_MANAGER.trySaveMappings(
                MappingsIdResolutionUtils.resolveNamespacedId(getMappingsState())
        );
        logMappings();
    }

    public static boolean updateMappingsToCustom(@NonNull String id) {
        final MappingsState mappingsState = ACTIVE_MAPPINGS_MANAGER.tryResolveCustomMappings(id);
        if (mappingsState == null)
            return false;
        currentMappingsState = mappingsState;
        ACTIVE_MAPPINGS_MANAGER.trySaveMappings(
                MappingsIdResolutionUtils.resolveNamespacedId(getMappingsState())
        );
        logMappings();
        return true;
    }

    public static boolean updateMappingsToBuiltIn(@NonNull String id) {
        final MappingsState old = currentMappingsState;
        currentMappingsState = ACTIVE_MAPPINGS_MANAGER.resolveMappings(
                MappingsIdResolutionUtils.resolveNamespacedId(MappingsState.Type.BUILTIN, id)
        );
        if (old != null && old.equals(currentMappingsState))
            return false;
        ACTIVE_MAPPINGS_MANAGER.trySaveMappings(
                MappingsIdResolutionUtils.resolveNamespacedId(getMappingsState())
        );
        logMappings();
        return true;
    }

    public static void updateMappingsToDefault() {
        currentMappingsState = ACTIVE_MAPPINGS_MANAGER.resolveMappings(
                MappingsIdResolutionUtils.resolveNamespacedId(MappingsState.Type.DEFAULT, "")
        );
        ACTIVE_MAPPINGS_MANAGER.trySaveMappings(
                MappingsIdResolutionUtils.resolveNamespacedId(getMappingsState())
        );
        logMappings();
    }
}
