package io.github.omegabird113.cmd_delete.commands;

import io.github.omegabird113.cmd_delete.actions.NavActionManager;
import io.github.omegabird113.cmd_delete.config.ActiveMappingsManager;
import io.github.omegabird113.cmd_delete.config.load.CustomMappingsJSONManager;
import io.github.omegabird113.cmd_delete.mappings.CustomNavMappings;
import io.github.omegabird113.cmd_delete.mappings.INavMappings;
import io.github.omegabird113.cmd_delete.mappings.NavMappingsManager;

import java.util.ArrayList;
import java.util.Arrays;

public final class MappingsInfoCollectionUtils {
    private MappingsInfoCollectionUtils() {
    }

    public static String getInfoFrom(INavMappings navMappings, boolean includeDescription) {
        float coverage = NavActionManager.getCoverage(navMappings);
        ActiveMappingsManager.Type type = NavMappingsManager.getActiveMappingsType();
        String typeString = type.toString();
        if (type == ActiveMappingsManager.Type.custom && !(navMappings instanceof CustomNavMappings)) {
            throw new RuntimeException("Custom mappings type declared, but an invalid class was provided.");
        }

        String namespacedId = switch (type) {
            case defaultMappings -> "default";
            case builtin ->
                    "builtin:" + Arrays.toString(navMappings.getMappingsSupportedSystems()).replace("[", "").replace("]", "").replace(", ", "_").toLowerCase();
            case custom -> "custom";
        };

        String displayName = switch (type) {
            case defaultMappings -> "Default mappings";
            case builtin ->
                    Arrays.toString(navMappings.getMappingsSupportedSystems()).replace("[", "").replace("]", "").replace(", ", " and ") + " mappings";
            case custom -> "\"" + ((CustomNavMappings) navMappings).getRegistry().getName() + "\"";
        };

        String description = switch (type) {
            case defaultMappings ->
                    "The default behaviour to auto-detect your OS and select the hard-coded mappings for your OS.";
            case builtin -> "Hard-coded mappings for the specified operating system(s).";
            case custom -> ((CustomNavMappings) navMappings).getRegistry().getDescription();
        };

        String baseString = displayName + " (id: " + namespacedId + ")";
        String descriptionString = "\nDescription:\n" + description;

        return includeDescription ? baseString + descriptionString : baseString;
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
