package io.github.omegabird113.cmd_delete.mappings;

import io.github.omegabird113.cmd_delete.LoggingManager;
import io.github.omegabird113.cmd_delete.command.MappingsInfoCollectionUtils;
import io.github.omegabird113.cmd_delete.config.ActiveMappingsManager;
import io.github.omegabird113.cmd_delete.config.MappingsIdResolutionUtils;
import org.slf4j.Logger;

import java.util.List;

public final class NavMappingsManager {
    private static final Logger LOGGER = LoggingManager.getInitializerLogger(NavMappingsManager.class);
    private static final NavMappings NAV_MAPPINGS = new NavMappings();
    private static final ActiveMappingsManager ACTIVE_MAPPINGS_MANAGER = new ActiveMappingsManager(
            NAV_MAPPINGS, Os.getCurrent()
    );
    private static MappingsState currentMappingsState;

    private NavMappingsManager() {
    }

    public static NavMappings getCurrentMappings() {
        return currentMappingsState.mappings();
    }

    private static void logMappings() {
        LOGGER.info("Mappings id \"{}\" loaded with supported systems \"{}\" and Coverage of {}% with a registry size of {}. It supports the actions: {}", MappingsIdResolutionUtils.resolveNamespacedId(currentMappingsState), List.of(currentMappingsState.mappings().getMappingsSupportedSystems()), getCurrentMappings().getCoverage() * 100, currentMappingsState.mappings().getRegistry().getSize(), currentMappingsState.mappings().getPossibleActions());
        LOGGER.info("The active mappings' info in \"/navmappings info\" will show as: \"{}\"", MappingsInfoCollectionUtils.getInfoFrom(currentMappingsState, false).replace("\n", " "));
        LOGGER.debug("Mappings state loaded: \"{}\"", currentMappingsState);
    }

    public static void loadMappings() {
        currentMappingsState = ACTIVE_MAPPINGS_MANAGER.tryGetMappings();
        ACTIVE_MAPPINGS_MANAGER.trySaveMappings(
                MappingsIdResolutionUtils.resolveNamespacedId(currentMappingsState)
        );
        logMappings();
    }

    public static boolean updateMappingsToCustom(String id) {
        MappingsState mappingsState = ACTIVE_MAPPINGS_MANAGER.tryResolveCustomMappings(id);
        if (mappingsState == null)
            return false;
        currentMappingsState = mappingsState;
        ACTIVE_MAPPINGS_MANAGER.trySaveMappings(
                MappingsIdResolutionUtils.resolveNamespacedId(currentMappingsState)
        );
        logMappings();
        return true;
    }

    public static void updateMappingsToBuiltIn(Os os) {
        currentMappingsState = ACTIVE_MAPPINGS_MANAGER.resolveMappings(
                MappingsIdResolutionUtils.resolveNamespacedId(MappingsState.Type.BUILTIN, os)
        );
        ACTIVE_MAPPINGS_MANAGER.trySaveMappings(
                MappingsIdResolutionUtils.resolveNamespacedId(currentMappingsState)
        );
        logMappings();
    }

    public static void updateMappingsToDefault() {
        currentMappingsState = ACTIVE_MAPPINGS_MANAGER.resolveMappings(
                MappingsIdResolutionUtils.resolveNamespacedId(MappingsState.Type.DEFAULT, "")
        );
        ACTIVE_MAPPINGS_MANAGER.trySaveMappings(
                MappingsIdResolutionUtils.resolveNamespacedId(currentMappingsState)
        );
        logMappings();
    }

    public static MappingsState getMappingsState() {
        return currentMappingsState;
    }
}
