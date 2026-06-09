package io.github.omegabird113.cmd_delete.command;

import io.github.omegabird113.cmd_delete.CmdDeleteClient;
import io.github.omegabird113.cmd_delete.actions.NavActionManager;
import io.github.omegabird113.cmd_delete.config.load.CustomMappingsJSONManager;
import io.github.omegabird113.cmd_delete.mappings.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public final class MappingsInfoCollectionUtils {
    private MappingsInfoCollectionUtils() {
    }

    public static String getInfoFrom(INavMappings navMappings, boolean includeDescription) {
        float coverage = NavActionManager.getCoverage(navMappings);

        String namespacedId;
        String displayName;
        String description;
        String version;

        if (navMappings instanceof CustomNavMappings custom) {
            namespacedId = "custom:" + custom.getRegistry().getFilename();
            displayName = "\"" + custom.getRegistry().getName() + "\"";
            description = custom.getRegistry().getDescription();
            version = custom.getRegistry().getVersion();
        } else if (navMappings instanceof MacNavMappings || navMappings instanceof WindowsLinuxNavMappings) {
            String[] systemStrings = Arrays.stream(navMappings.getMappingsSupportedSystems())
                    .map(Os::toString)
                    .toArray(String[]::new);
            namespacedId = "builtin:" + String.join("_", systemStrings).toLowerCase(Locale.ROOT);
            displayName = String.join(" and ", systemStrings) + " mappings";
            description = "Hard-coded mappings for the specified operating system(s).";
            version = CmdDeleteClient.VERSION;
        } else {
            namespacedId = "unknown";
            displayName = "unknown";
            description = "unknown";
            version = CmdDeleteClient.VERSION;
        }

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
        return internal.toArray(String[]::new);
    }
}
