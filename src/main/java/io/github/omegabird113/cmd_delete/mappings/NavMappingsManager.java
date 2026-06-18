package io.github.omegabird113.cmd_delete.mappings;

import io.github.omegabird113.cmd_delete.CmdDeleteClient;
import io.github.omegabird113.cmd_delete.actions.NavActionUtils;
import io.github.omegabird113.cmd_delete.command.MappingsInfoCollectionUtils;
import io.github.omegabird113.cmd_delete.config.ActiveMappingsManager;

import java.util.List;

public final class NavMappingsManager {
    private static final NavMappings NAV_MAPPINGS = new NavMappings();
    private static final ActiveMappingsManager activeMappingsManager = new ActiveMappingsManager(
            NAV_MAPPINGS, Os.getCurrent()
    );
    private static MappingsState currentMappingsState;

    private NavMappingsManager() {
    }

    public static NavMappings getCurrentMappings() {
        return currentMappingsState.mappings();
    }

    private static void logMappings() {
        CmdDeleteClient.LOGGER.info("Mappings id \"{}\" loaded with supported systems \"{}\" and Coverage of {}%. Its info shows as: \"{}\"", activeMappingsManager.resolveNamespacedId(currentMappingsState), List.of(currentMappingsState.mappings().getMappingsSupportedSystems()), NavActionUtils.getCoverage(getCurrentMappings()) * 100, MappingsInfoCollectionUtils.getInfoFrom(currentMappingsState, false).replace("\n", " "));
        CmdDeleteClient.LOGGER.debug("Mappings registry: {}", currentMappingsState.mappings().getRegistry());
    }

    public static void loadMappings() {
        currentMappingsState = activeMappingsManager.tryGetMappings();
        activeMappingsManager.trySaveMappings(
                activeMappingsManager.resolveNamespacedId(currentMappingsState)
        );
        logMappings();
    }

    public static boolean updateMappingsToCustom(String id) {
        MappingsState mappingsState = activeMappingsManager.tryResolveCustomMappings(id);
        if (mappingsState == null)
            return false;
        currentMappingsState = mappingsState;
        activeMappingsManager.trySaveMappings(
                activeMappingsManager.resolveNamespacedId(currentMappingsState)
        );
        logMappings();
        return true;
    }

    public static void updateMappingsToBuiltIn(Os os) {
        currentMappingsState = activeMappingsManager.resolveMappings(
                activeMappingsManager.resolveNamespacedId(MappingsState.Type.BUILTIN, os)
        );
        activeMappingsManager.trySaveMappings(
                activeMappingsManager.resolveNamespacedId(currentMappingsState)
        );
        logMappings();
    }

    public static void updateMappingsToDefault() {
        currentMappingsState = activeMappingsManager.resolveMappings(
                activeMappingsManager.resolveNamespacedId(MappingsState.Type.DEFAULT, "")
        );
        activeMappingsManager.trySaveMappings(
                activeMappingsManager.resolveNamespacedId(currentMappingsState)
        );
        logMappings();
    }

    public static MappingsState getMappingsState() {
        return currentMappingsState;
    }
}
