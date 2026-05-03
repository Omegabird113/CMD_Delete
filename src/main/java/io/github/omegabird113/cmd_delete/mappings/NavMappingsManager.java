package io.github.omegabird113.cmd_delete.mappings;

import io.github.omegabird113.cmd_delete.CmdDeleteClient;
import io.github.omegabird113.cmd_delete.actions.NavActionManager;
import io.github.omegabird113.cmd_delete.config.ActiveMappingsManager;

public class NavMappingsManager {
    private static final INavMappings WINDOWS_MAPPINGS = new WindowsLinuxNavMappings();
    private static final INavMappings MAC_MAPPINGS = new MacNavMappings();
    private static final INavMappings LINUX_MAPPINGS = new WindowsLinuxNavMappings();
    private static final INavMappings CUSTOM_MAPPINGS = new CustomNavMappings();

    private static final ActiveMappingsManager activeMappingsManager = new ActiveMappingsManager(
            WINDOWS_MAPPINGS, MAC_MAPPINGS, LINUX_MAPPINGS, CUSTOM_MAPPINGS, getOs()
    );

    private static ActiveMappingsManager.MappingWithState currentMappingsState;

    public static INavMappings getCurrentMappings() {
        return currentMappingsState.mappings();
    }

    public static void LoadMappings() {
        currentMappingsState = activeMappingsManager.tryGetMappings();
        CmdDeleteClient.LOGGER.info("The loaded mappings have {}% coverage with supported actions: {}", NavActionManager.getCoverage(getCurrentMappings()) * 100, getCurrentMappings().getPossibleActions());
    }

    public static void updateMappingsToCustom(String id) {
        currentMappingsState = activeMappingsManager.resolveMappings(
                activeMappingsManager.resolveNamespacedId(ActiveMappingsManager.Type.custom, id)
        );
        activeMappingsManager.trySaveMappings(
                activeMappingsManager.resolveNamespacedId(currentMappingsState)
        );
    }

    public static void updateMappingsToBuiltIn(Os os) {
        currentMappingsState = activeMappingsManager.resolveMappings(
                activeMappingsManager.resolveNamespacedId(ActiveMappingsManager.Type.builtin, os)
        );
        activeMappingsManager.trySaveMappings(
                activeMappingsManager.resolveNamespacedId(currentMappingsState)
        );
    }

    public static void updateMappingsToDefault() {
        currentMappingsState = activeMappingsManager.resolveMappings(
                activeMappingsManager.resolveNamespacedId(ActiveMappingsManager.Type.defaultMappings, "")
        );
        activeMappingsManager.trySaveMappings(
                activeMappingsManager.resolveNamespacedId(currentMappingsState)
        );
    }

    public static Os getOs() {
        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            return Os.MAC;
        } else if (System.getProperty("os.name").toLowerCase().contains("win")) {
            return Os.WINDOWS;
        } else {
            return Os.LINUX;
        }
    }
}