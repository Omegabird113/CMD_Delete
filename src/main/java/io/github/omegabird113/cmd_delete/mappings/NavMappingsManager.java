package io.github.omegabird113.cmd_delete.mappings;

import io.github.omegabird113.cmd_delete.utils.LoggingManager;
import io.github.omegabird113.cmd_delete.command.MappingsInfoCollectionUtils;
import io.github.omegabird113.cmd_delete.config.data.FeatureFlags;
import io.github.omegabird113.cmd_delete.config.data.MappingsIdResolutionUtils;
import io.github.omegabird113.cmd_delete.config.data.MappingsRegistry;
import io.github.omegabird113.cmd_delete.config.fileio.ActiveMappingsManager;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;
import java.util.Optional;

public final class NavMappingsManager {
    private static final @NonNull Logger LOGGER = LoggingManager.getLogger(NavMappingsManager.class);
    private static @Nullable MappingsState currentMappingsState;

    private NavMappingsManager() {
    }

    public static @NonNull MappingsState getMappingsState() {
        if (currentMappingsState == null)
            throw new IllegalStateException("No current mappings state has been set, but the mappings were accessed");
        return currentMappingsState;
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
        LOGGER.debug("Mappings state loaded: \"{}\"", currentMappingsState);
    }

    public static void loadMappings() {
        currentMappingsState = ActiveMappingsManager.tryGetMappings();
        ActiveMappingsManager.trySaveMappings(
                MappingsIdResolutionUtils.resolveNamespacedId(getMappingsState())
        );
        logMappings();
    }

    private static boolean updateMappingsTo(@NonNull MappingsType type, @NonNull String id) {
        final MappingsState old = currentMappingsState;
        currentMappingsState = ActiveMappingsManager.resolveMappings(
                MappingsIdResolutionUtils.resolveNamespacedId(type, id)
        );
        if (old != null && old.equals(currentMappingsState))
            return false;
        ActiveMappingsManager.trySaveMappings(
                MappingsIdResolutionUtils.resolveNamespacedId(getMappingsState())
        );
        logMappings();
        return true;
    }

    public static boolean updateMappingsToCustom(@NonNull String id) {
        return updateMappingsTo(MappingsType.CUSTOM, id);
    }

    public static boolean updateMappingsToBuiltIn(@NonNull String id) {
        return updateMappingsTo(MappingsType.BUILTIN, id);
    }

    public static void updateMappingsToDefault() {
        boolean success = updateMappingsTo(MappingsType.DEFAULT, "");
        if (!success)
            throw new IllegalStateException("Failed to load default mappings");
    }
}
