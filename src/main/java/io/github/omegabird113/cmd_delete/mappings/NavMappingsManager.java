package io.github.omegabird113.cmd_delete.mappings;

import io.github.omegabird113.cmd_delete.CmdDeleteClient;
import io.github.omegabird113.cmd_delete.actions.NavActionManager;

import java.util.List;

abstract public class NavMappingsManager {
    private static INavMappings currentMappings;
    private static boolean useCustomMapping = false;

    public static INavMappings getCurrentMappings() {
        return currentMappings;
    }

    public static void LoadMappings() {
        currentMappings = getOsMappings();
        CmdDeleteClient.LOGGER.info("OS nav Mappings loaded for systems: {}", List.of(currentMappings.getMappingsSupportedSystems()));
        CmdDeleteClient.LOGGER.info("The loaded mappings have {}% coverage with supported actions: {}", NavActionManager.getCoverage(currentMappings) * 100, currentMappings.getPossibleActions());
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

    private static INavMappings getOsMappings() {
        if (getOs() == Os.MAC)
            return new MacNavMappings();
        return new WindowsLinuxNavMappings();
    }
}
