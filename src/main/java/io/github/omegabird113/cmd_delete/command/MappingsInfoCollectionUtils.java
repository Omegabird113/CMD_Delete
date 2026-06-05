package io.github.omegabird113.cmd_delete.command;

import io.github.omegabird113.cmd_delete.CmdDeleteClient;
import io.github.omegabird113.cmd_delete.actions.NavActionManager;
import io.github.omegabird113.cmd_delete.config.load.CustomMappingsJSONManager;
import io.github.omegabird113.cmd_delete.mappings.CustomNavMappings;
import io.github.omegabird113.cmd_delete.mappings.INavMappings;
import io.github.omegabird113.cmd_delete.mappings.MacNavMappings;
import io.github.omegabird113.cmd_delete.mappings.WindowsLinuxNavMappings;

import java.util.ArrayList;
import java.util.Arrays;

public final class MappingsInfoCollectionUtils {
    private MappingsInfoCollectionUtils() {
    }

    public static String getInfoFrom(INavMappings navMappings, boolean includeDescription) {
        float coverage = NavActionManager.getCoverage(navMappings);

        String namespacedId = switch (navMappings) {
            case INavMappings n when n instanceof MacNavMappings || n instanceof WindowsLinuxNavMappings ->
                    "builtin:" + Arrays.toString(navMappings.getMappingsSupportedSystems()).replace("[", "").replace("]", "").replace(", ", "_").toLowerCase();
            case INavMappings n when n instanceof CustomNavMappings ->
                    "custom";
            default -> "unknown";
        };

        String displayName = switch (navMappings) {
            case INavMappings n when n instanceof MacNavMappings || n instanceof WindowsLinuxNavMappings ->
                    Arrays.toString(navMappings.getMappingsSupportedSystems()).replace("[", "").replace("]", "").replace(", ", " and ") + " mappings";
            case INavMappings n when n instanceof CustomNavMappings ->
                    "\"" + ((CustomNavMappings) navMappings).getRegistry().getName() + "\"";
            default -> "unknown";
        };

        String description = switch (navMappings) {
            case INavMappings n when n instanceof MacNavMappings || n instanceof WindowsLinuxNavMappings ->
                    "Hard-coded mappings for the specified operating system(s).";
            case INavMappings n when n instanceof CustomNavMappings ->
                    ((CustomNavMappings) navMappings).getRegistry().getDescription();
            default -> "unknown";
        };

        String version = switch (navMappings) {
            case INavMappings n when n instanceof CustomNavMappings ->
                    ((CustomNavMappings) navMappings).getRegistry().getVersion();
            default -> CmdDeleteClient.VERSION;
        };

        String baseString = displayName + " (id: " + namespacedId + ") v" + version;
        String descriptionString = "\nDescription:\n" + description;
        String coverageString = "\nThese mappings have " + String.format("%.2f", coverage * 100) + "% coverage.";

        return includeDescription ? baseString + coverageString + descriptionString : baseString + coverageString;
    }

    public static String[] getMappingsList() {
        ArrayList<String> internal = new ArrayList<>();
        internal.add("default");
        internal.add("builtin:windows_linux");
        internal.add("builtin:mac");
        internal.addAll(CustomMappingsJSONManager.getAvailableOptions());
        return internal.toArray(new String[0]);
    }
}
